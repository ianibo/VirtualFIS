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
import static groovyx.net.http.ContentType.JSON

class ReconciliationService {

  def mongoService 
  def executorService
  def grailsApplication 

  def active_jobs = new java.util.HashMap()

  def getStatus(internal_org_id, remote_source_id, remote_collection_id) {
    log.debug("getStatus");
    // See if we currently have an active reconcilliation process for the identified org, at the remote source
    // (Using the identifier particular to that remote source)
    def result = [active:false] // Default is no-job

    log.debug(active_jobs)

    try {
      synchronized(active_jobs) {
        result.job = active_jobs["${internal_org_id}:${remote_source_id}:${remote_collection_id}".toString()]
      }
    }
    catch ( Exception e ) {
    }

    if ( result.job ) {
      log.debug("Running job")
      // There is an active job
      result.active = true
    }
    else {
      log.debug("No active job")
    }

    result
  }

  def startReconciliation(internal_org_id, remote_source_id, remote_collection_id) {
    log.debug("startReconciliation ${internal_org_id}:${remote_source_id}:${remote_collection_id}");
    def job_id ="${internal_org_id}:${remote_source_id}:${remote_collection_id}".toString()
    def result
    try {
      synchronized(active_jobs) {
        // Check that there isn't already a running job!
        result = active_jobs[job_id]
        if ( result ) {
          log.warn("Attempt to start a job already running.. returning existing info")
        }
        else {
          result = [:]
          log.debug("New job id will be ${job_id}");
          active_jobs[job_id] = result;
          def future = executorService.submit({
              reconcile(internal_org_id, remote_source_id, remote_collection_id)
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

  def reconcile(internal_org_id, remote_source_id, remote_collection_id) {

    def task_id = "${internal_org_id}:${remote_source_id}:${remote_collection_id}".toString()
    log.debug("reconcile ${task_id}");

    try {

      // Iterate over all
      iterateAllRecords(remote_source_id, remote_collection_id) { record ->
        try {
          log.debug("Inside closure......");
        }
        catch ( Exception e ) {
          log.error("Problem",e)
        }
        finally {
          log.debug("Closure finally");
        }
      }

      log.debug("reconcile complete, remove ${task_id} from active jobs - before: ${active_jobs.keySet()}")
      def v = active_jobs.remove(task_id);
      log.debug("reconcile complete, remove from active jobs - after: ${active_jobs.keySet()} ${v}")
    }
    catch ( Exception e ) {
      log.error("Problem",e);
    }
   
  }
 
  def iterateAllRecords(sourceid, collectionid, processing_closure) {
    log.debug("iterateAllRecords ${sourceid},${collectionid}");
    // aggregator.openfamilyservices.org.uk/dpp/oai?verb=listRecords&metadataPrefix=oai_dc&apikey=583c10bfdbd326ba:43918c3b:131c83c6954:-606c
    // Currently we only deal with 1 source - OFS, so really we only need the collection for now
    def apikey = grailsApplication.config.ofsapikey
    log.debug("Iterate all records, call closure, apikey = ${apikey}");
    sync(processing_closure);
  } 


  def sync(processing_closure) {
    log.debug("sync");

    def props =[:]
    props.reccount = 0;
    props.maxts = "";

    // def oai_endpoint = new RESTClient( 'http://aggregator.openfamilyservices.org.uk/dpp/oai' )
    def solr_endpoint = new RESTClient( 'http://aggregator.openfamilyservices.org.uk/index/aggr/select' )

    def hasMore = true
    def start = 0;
    while ( start >= 0 ) {
      try {
        Thread.sleep(5000);
      }
      catch ( Exception e ) {
      }

      log.debug("Iterating using resumption token");
      start = fetchSOLRPage(solr_endpoint, start, 'Surrey_County_Council', processing_closure);
    }
  }

  def fetchSOLRPage(endpoint, start, authcode, closure) {

    endpoint.request(GET, JSON) {request ->
      uri.query = [
        q:"authority_shortcode:${authcode}",
        fl:'aggregator.internal.id,modified,repo_url_s,restp',
        wt:'json',
        rows:'100',
        start:start
      ]
      request.getParams().setParameter("http.socket.timeout", new Integer(5000))
      headers.Accept = 'application/json'

      response.success = { resp, json ->
        log.debug( "Server Response: ${resp.statusLine}" )
        log.debug( "Server Type: ${resp.getFirstHeader('Server')}" )
        log.debug( "content type: ${resp.headers.'Content-Type'}" )
        json.response.docs.each { doc ->
          log.debug("doc = ${doc}")
        }
      }

      response.failure = { resp ->
        log.debug( "OAI ERROR: ${resp.statusLine}" )
      }
    }

    start = -1;
    return start
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

          try {
            Thread.sleep(500);
          }
          catch ( Exception e ) {
          }
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

}
