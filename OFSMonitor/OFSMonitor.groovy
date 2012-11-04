#!/usr/bin/groovy

@GrabResolver(name='es', root='https://oss.sonatype.org/content/repositories/releases')
// @Grab(group='com.gmongo', module='gmongo', version='0.9.2')
@Grapes([
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2'),
  @Grab(group='com.gmongo', module='gmongo', version='0.9.2'),
  @Grab(group='org.apache.httpcomponents', module='httpclient', version='4.0'),
  @Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0'),
  @Grab( 'log4j:log4j:1.2.14' ),
  @Grab('xom:xom:1.2.5')

])

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
// import grails.converters.*
import net.sf.json.xml.XMLSerializer
import org.apache.log4j.Level
import org.apache.log4j.Logger
// import static groovyx.net.http.ContentType.JSON


mongoService=null
apikey=args[0]

log = Logger.getLogger(this.class)
Logger.rootLogger.level = Level.DEBUG

// initialise the service
init();

def policy = [:]
def job_info = [:]
policy.new_record='auto'

mdb = mongoService.getDB('localchatter')

reconcile("Sheffield_City_Council","Sheffield_City_Council","Sheffield_City_Council",job_info,mdb,policy);

log.debug("Done");

def init() {
  println("Init...");

  log.debug("Init mongo service");
  def options = new com.mongodb.MongoOptions()
  options.socketKeepAlive = true
  options.autoConnectRetry = true
  options.slaveOk = true
  mongoService = new com.gmongo.GMongo('127.0.0.1', options);


  log.debug("Registering json null encoder")
  def jsonnull_encoder = new org.bson.Transformer() {
    Object transform(Object o) {
      return null
    }
  };
  // Attempt to rewrite input field names, substituting . for _
  def jsonobject_encoder = new org.bson.Transformer() {
    // Transform the json object into a simple hashmap, where the keys have . replaced with _
    Object transform(Object o) {
      // def result = new org.bson.BasicBSONObject()
      def result = new java.util.HashMap()
      net.sf.json.JSONObject jo = (net.sf.json.JSONObject) o
      jo.keys().each { key ->
        result.put(key.replace('.','_'), jo.get(key))
      }
      return result
    }
  };
  org.bson.BSON.addEncodingHook(net.sf.json.JSONNull,jsonnull_encoder)
  org.bson.BSON.addEncodingHook(net.sf.json.JSONObject,jsonobject_encoder)
}

def reconcile(internal_org_id, 
              remote_source_id, 
              remote_collection_id, 
              job_info, 
              mdb, 
              policy) {


  log.debug("*** reconcile ${job_info.job_id}")
  println("*** reconcile ${job_info.job_id}")
  try {
    // Iterate over all
    iterateAllRecords(remote_source_id, remote_collection_id, job_info) { record_identifier, record_type, display_text, record_content ->
      try {
        log.debug("Inside closure...... ${record_type}, ${record_identifier}");
        // Reconcilliation phase 1 : See if we have a previous copy of this remote record
        def recon_rec_info = mdb.localCopies.find(reconSource:job_info.job_id, docid:record_identifier)
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
  log.debug("Iterate all records, call closure, apikey = ${apikey}");
  sync(processing_closure, job_info, collectionid);
} 


def sync(processing_closure, job_info, collectionid) {
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
    start = fetchSOLRPage(solr_endpoint, start, collectionid, job_info, processing_closure);
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

    log.debug("Searching /index/aggr/select?q=authority_shortcode:${authcode}");

    response.success = { resp, json ->
      log.debug( "Server Response: ${resp.statusLine}" )
      log.debug( "Server Type: ${resp.getFirstHeader('Server')}" )
      log.debug( "content type: ${resp.headers.'Content-Type'}" )

      
      json.response.docs.each { doc ->
        log.debug("doc = [${start++}] ${doc}")

        def newrec = fetchRecord(endpoint,doc.repo_url_s[0], doc.restp)
        if ( newrec ) {
          closure.call(doc['dc.identifier'], "OFS:${doc.restp}", doc['dc.title'][0], newrec)
        }
        else {
          log.error("Fetch record returned NULL!")
        }
      }

      int num_found = json.response.numFound
      if ( start == num_found ) {
        log.debug("Reached end of records, (start=${start},num_found=${num_found})break out");
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
def fetchRecord(endpoint, path, restp) {
  log.debug("Requesting ${path}?apikey=${apikey}")
  def result = null


  try {
    // endpoint.request(GET, ContentType.XML) {request ->
    // Request XML as we want to use the JSON XML to JSON parser instead
    endpoint.request(GET, ContentType.TEXT) {request ->
      uri.path = path
      uri.query = [
        'apikey':apikey
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
       
        result."$schema" = "http://purl.org/jsonschema/${restp}";

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
