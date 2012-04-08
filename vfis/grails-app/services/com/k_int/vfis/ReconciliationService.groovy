package com.k_int.vfis

import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.*
import java.nio.charset.Charset
import groovyx.net.http.RESTClient
import groovy.util.slurpersupport.GPathResult
import groovy.xml.StreamingMarkupBuilder
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.*
import java.nio.charset.Charset
import static groovyx.net.http.Method.GET
import grails.converters.*
import net.sf.json.xml.XMLSerializer
// import static groovyx.net.http.ContentType.JSON

class ReconciliationService {

  def mongoService 
  def executorService
  def grailsApplication 

  @javax.annotation.PostConstruct
  def init() {
    log.debug("Registering json null encoder")
    def jsonnull_encoder = new org.bson.Transformer() {
      Object transform(Object o) {
        return null
      }
    };
    org.bson.BSON.addEncodingHook(net.sf.json.JSONNull,jsonnull_encoder)
  }


  def active_jobs = new java.util.HashMap()

  def getStatus(internal_org_id, remote_source_id, remote_collection_id) {
    log.debug("getStatus");
    // See if we currently have an active reconcilliation process for the identified org, at the remote source
    // (Using the identifier particular to that remote source)
    def result = [active:false] // Default is no-job
    def job_id ="${internal_org_id}:${remote_source_id}:${remote_collection_id}".toString()

    log.debug(active_jobs)

    try {
      synchronized(active_jobs) {
        result.job = active_jobs[job_id]
      }
    }
    catch ( Exception e ) {
    }

    if ( result.job ) {
      log.debug("There is a Running job for ${job_id}")
      // There is an active job
      result.active = true
    }
    else {
      log.debug("No active job")
    }

    result
  }

  def startReconciliation(internal_org_id, remote_source_id, remote_collection_id, policy) {
    def job_id ="${internal_org_id}:${remote_source_id}:${remote_collection_id}".toString()
    log.debug("** startReconciliation ${internal_org_id}:${remote_source_id}:${remote_collection_id}");
    def result
    try {
      synchronized(active_jobs) {
        // Check that there isn't already a running job!
        result = active_jobs[job_id]
        if ( result ) {
          log.warn("Attempt to start a job already running.. returning existing info")
        }
        else {
          // obtain / Set up a record to track info about this source
          def mdb = mongoService.getMongo().getDB('vfis')
          def recon_status = mdb.reconciliationSources.findOne(internalId:job_id)
          if ( recon_status ) {
            log.debug("Located existing reconciliation status object in db")
          } 
          else {
            log.debug("First sighting of a reconciliation for ${job_id}")
            recon_status = [:]
            recon_status.internalId = job_id;
          }

          // Queue up the request
          result = [:]
          result.job_id = job_id;
          log.debug("New job id will be ${job_id}");
          active_jobs[job_id] = result;
          def future = executorService.submit({
              reconcile(internal_org_id, remote_source_id, remote_collection_id, result, mdb, recon_status, policy)
          } as java.util.concurrent.Callable)
          result.future = future
        }
      }
    }
    catch ( Exception e ) {
      log.error("Problem",e)
    }

    log.debug("startReconciliation returns");
    result
  }

  def reconcile(internal_org_id, remote_source_id, remote_collection_id, job_info, mdb, recon_status, policy) {

    log.debug("*** reconcile ${job_info.job_id}")
    try {
      // Update persistent info
      recon_status.lastStarted = System.currentTimeMillis();
      mdb.reconciliationSources.save(recon_status);

      // Iterate over all
      iterateAllRecords(remote_source_id, remote_collection_id, job_info) { record_identifier, record_type, display_text, record_content ->
        try {
          log.debug("Inside closure...... ${record_type}, ${record_identifier}");
          // Reconcilliation phase 1 : See if we have a previous copy of this remote record
          def recon_rec_info = mdb.reconRecords.find(reconSource:job_info.job_id, docid:record_identifier)
          if ( recon_rec_info ) {
            // Yes, we need to determine if anything has changed
          }
          else {
            // No, this is a new record in the tracking database for starters
            recon_rec_info = [:]
            recon_rec_info.docid = record_identifier
            recon_rec_info.lastmod = System.currentTimeMillis()
            recon_rec_info.reconSource = job_info.job_id
            recon_rec_info.displayText = display_text
            recon_rec_info.src = record_content

            // Is the reconciliation policy for new records to auto-create them?
            if ( policy.new_record == 'auto') {
              recon_rec_info.changes = [[desc:"Record ${record_identifier} newly created.", status:'autoaccepted']]
              def content_record = [:]
              content_record.docid = record_identifier
              content_record.owner = internal_org_id
              content_record.src = record_content
              content_record.type = record_type
              content_record._id = new org.bson.types.ObjectId()
              recon_rec_info.targetRecord = content_record._id;
              mdb.content.save(content_record);
            }
            else {
              log.debug("No new record policy")
            }
          }
          mdb.reconRecords.save(recon_rec_info)
        }
        catch ( Exception e ) {
          log.error("Problem",e)
        }
        finally {
          log.debug("Closure finally");
        }
      }

      log.debug("reconcile complete, remove ${job_info.job_id} from active jobs - before: ${active_jobs.keySet()}")
      def v = active_jobs.remove(job_info.job_id);
      log.debug("reconcile complete, remove from active jobs - after: ${active_jobs.keySet()} ${v}")
    }
    catch ( Exception e ) {
      log.error("Problem",e);
    }
    log.debug("reconcile complete for ${job_info.job_id}")
  }
 
  def iterateAllRecords(sourceid, collectionid, job_info, processing_closure) {
    log.debug("iterateAllRecords ${sourceid},${collectionid}");
    // aggregator.openfamilyservices.org.uk/dpp/oai?verb=listRecords&metadataPrefix=oai_dc&apikey=583c10bfdbd326ba:43918c3b:131c83c6954:-606c
    // Currently we only deal with 1 source - OFS, so really we only need the collection for now
    def apikey = grailsApplication.config.ofsapikey
    log.debug("Iterate all records, call closure, apikey = ${apikey}");
    sync(processing_closure, job_info);
  } 


  def sync(processing_closure, job_info) {
    log.debug("sync");

    def props =[:]
    props.reccount = 0;
    props.maxts = "";

    def solr_endpoint = new RESTClient( 'http://aggregator.openfamilyservices.org.uk/' )

    def hasMore = true
    def start = 0;
    while ( start >= 0 ) {
      try {
        Thread.sleep(500);
      }
      catch ( Exception e ) {
      }

      log.debug("Next start position is ${start}, selecting page of data");
      start = fetchSOLRPage(solr_endpoint, start, 'Surrey_County_Council', job_info, processing_closure);
    }
  }

  def fetchSOLRPage(endpoint, start, authcode, job_info, closure) {

    // def mdb = mongoService.getMongo().getDB('vfis')

    endpoint.request(GET, groovyx.net.http.ContentType.JSON) {request ->
      uri.path = 'index/aggr/select'
      uri.query = [
        q:"authority_shortcode:${authcode}",
        fl:'aggregator.internal.id,modified,repo_url_s,restp,dc.identifier,repo_url_s,dc.title',
        wt:'json',
        rows:'100',
        start:start
      ]
      request.getParams().setParameter("http.socket.timeout", new Integer(10000))
      headers.Accept = 'application/json'

      response.success = { resp, json ->
        log.debug( "Server Response: ${resp.statusLine}" )
        log.debug( "Server Type: ${resp.getFirstHeader('Server')}" )
        log.debug( "content type: ${resp.headers.'Content-Type'}" )

        
        json.response.docs.each { doc ->
          log.debug("doc = [${start++}] ${doc}")
  
          def newrec = fetchRecord(endpoint,doc.repo_url_s[0])
          if ( newrec ) {
            closure.call(doc['dc.identifier'], "OFS:${doc.restp}", doc['dc.title'][0], newrec)
          }
          else {
            log.error("Fetch record returned NULL!")
          }
        }

        int num_found = json.response.numFound
        if ( start == num_found ) {
          log.debug("Reached end of records, break out");
          start = -1
        }
        else {
          log.debug("Processing, next start position is ${start} / ${num_found}")
          job_info.start = start
          job_info.max = num_found
        }
      }

      response.failure = { resp ->
        log.debug( "OAI ERROR: ${resp.statusLine}" )
      }
    }

    start
  }


  //
  // Use the org.json classes to try and parse the XML into a JSON object
  //
  def fetchRecord(endpoint, path) {
    log.debug("Requesting ${path}?apikey=${grailsApplication.config.ofsapikey}")
    def result = null


    try {
      // endpoint.request(GET, ContentType.XML) {request ->
      // Request XML as we want to use the JSON XML to JSON parser instead
      endpoint.request(GET, ContentType.TEXT) {request ->
        uri.path = path
        uri.query = [
          'apikey':grailsApplication.config.ofsapikey
        ]
        headers.Accept = 'application/xml'
        request.getParams().setParameter("http.socket.timeout", new Integer(10000))

        response.success = { resp, reader ->
          response.headers.each { h ->
            log.debug(h);
          }
          def xml_text = reader.text
          log.debug("Fetched record.....${resp}. Trying to convert.. construct")
          def xs=new net.sf.json.xml.XMLSerializer();
          xs.setSkipNamespaces( true );  
          xs.setTrimSpaces( true );  
          xs.setRemoveNamespacePrefixFromElements( true );  
          // result = net.sf.json.JSONObject.toBean(xs.read(xml_text))
          // mongo likes to have nulls and not JSONNull for it's null fields... Do some mapping
          result = xs.read(xml_text)
          log.debug("Result of conversion: ${result}")
          // result = xs.read("<a><b>Hello</b><c>Goodbye</c></a>")
          //log.debug("\n\nConverted\n\n ${result}")          
        }

        response.failure = { resp, reader ->
          log.debug( "Record fetch error ${resp}" )
          System.out << reader
        }
      }
    }
    catch ( Exception e ) {
      log.error("problem ${e}")
    }
    finally {
      log.debug("Fetch doc complete")
    }

    result
  }

  def recurse(gpath) {
    gpath.childNodes().each { cn ->
      recurse(cn)
    }
  }

  def fetchOAIPage(oai_endpoint,
                   resumption_token,
                   props,
                   prefix,
                   setname,
                   processing_closure) {
    log.debug("fetchOAIPage ${resumption_token}, ${prefix}, ${setname}");

    def result = null;

    oai_endpoint.request(GET) {request ->

      // uri.path = '/ajax/services/search/web'
      if ( resumption_token != null ) {
        log.debug("Processing with resumption token...");
        uri.query = [ 'verb':'ListRecords',
                      'resumptionToken':resumption_token,
                      'apikey':grailsApplication.config.ofsapikey
                      ]  // from, until,...
      }
      else {
        log.debug("Initial harvest - no resumption token");
        uri.query = [ 'verb':'ListRecords',
                      'metadataPrefix':prefix,
                      'set': setname,
                      'apikey':grailsApplication.config.ofsapikey ]  // from, until,...
      }

      request.getParams().setParameter("http.socket.timeout", new Integer(5000))
      headers.Accept = 'application/xml'

      // headers.'User-Agent' = 'GroovyHTTPBuilderTest/1.0'
      // headers.'Referer' = 'http://blog.techstacks.com/'
      response.success = { resp, xml ->
        log.debug( "Server Response: ${resp.statusLine}" )
        log.debug( "Server Type: ${resp.getFirstHeader('Server')}" )
        log.debug( "content type: ${resp.headers.'Content-Type'}" )

        log.debug("Iterate....");
        xml?.ListRecords?.record.each { rec ->
          log.debug("Record under xml ${rec.toString()}");
          // def builder = new StreamingMarkupBuilder()
          // log.debug("record: ${builder.bindNode(rec.metadata.description).toString()}")
          // def new_record = builder.bindNode(rec.metadata.children()[0]).toString()
          // log.debug("submit record[${props.reccount++}]")
          // byte[] db = new_record.getBytes('UTF-8')

          props.maxts = rec.header.datestamp;

          log.debug("About call closure [${props.reccount} / ${props.maxts}]");
          processing_closure.call(rec)
          // uploadStream(db,aggregator_service, 'nmcg')
        }

        result = xml?.ListRecords?.resumptionToken?.toString()
      }

      response.failure = { resp ->
        log.debug( "OAI ERROR: ${resp.statusLine}" )
      }
    }

    log.debug("fetch page returning ${result}.");

    result
  }

  /// Convert FSD XML to internal canonical JSON
  def convertFSD(newrec) {
    log.debug("Convert FSD ${newrec}");
    def result = [:]
    result.one='hello'
    result
  }

  /// Convert ECD XML to internal canonical JSON
  def convertECD(newrec) {
    log.debug("Convert ECD ${newrec}");
    def result = [:]
    result.two='goodbye'
    result
  }
}
