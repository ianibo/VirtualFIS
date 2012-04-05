package com.k_int.vfis

class ReconciliationService {

  def mongoService 
  def executorService

  def active_jobs = [:]

  def getStatus(internal_org_id, remote_source_id, remote_collection_id) {
    log.debug("getStatus");
    // See if we currently have an active reconcilliation process for the identified org, at the remote source
    // (Using the identifier particular to that remote source)
    def result = [active:false] // Default is no-job

    try {
      synchronized(active_jobs) {
        result.job = active_jobs["${internal_org_id}:${remote_source_id}:${remote_collection_id}"]
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
      result.job = startReconciliation(internal_org_id, remote_source_id, remote_collection_id)
      result.active = true
    }
  }

  def startReconciliation(internal_org_id, remote_source_id, remote_collection_id) {
    log.debug("startReconciliation");
    def result = [:]
    try {
      synchronized(active_jobs) {
        active_jobs["${internal_org_id}:${remote_source_id}:${remote_collection_id}"] = result;
        def future = executorService.submit({
            reconcile(internal_org_id, remote_source_id, remote_collection_id)
        } as Callable)
        result.future = future
      }
    }
    catch ( Exception e ) {
    }

    result
  }

  def reconcile(internal_org_id, remote_source_id, remote_collection_id) {
    log.debug("reconcile ${internal_org_id}, ${remote_source_id}, ${remote_collection_id}");
    // Iterate over all
    iterateAllRecords(remote_source_id, remote_collection_id) { record ->
      log.debug("processing closure...${p}");
    }
    log.debug("reconcile complete")
  }
 
  def iterateAllRecords(sourceid, collectionid, processing_closure) {
    log.debug("iterateAllRecords ${sourceid},${collectionid}");
    // Currently we only deal with 1 source - OFS, so really we only need the collection for now
    def p = [:]
    processing_closure.call(p)
  } 
}
