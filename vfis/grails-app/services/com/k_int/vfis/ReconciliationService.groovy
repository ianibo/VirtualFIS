package com.k_int.vfis

class ReconciliationService {

  def mongoService 
  def executorService

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

    try {
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
    // Currently we only deal with 1 source - OFS, so really we only need the collection for now
    def p = [:]
    log.debug("Iterate all records, call closure");
    processing_closure.call(p)
    processing_closure.call(p)
    processing_closure.call(p)
    processing_closure.call(p)
    processing_closure.call(p)
  } 
}
