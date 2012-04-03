package com.k_int.vfis

class ReconciliationService {

  def active_jobs = [:]

  def getStatus(internal_org_id, remote_source_id, remote_collection_id) {
    // See if we currently have an active reconcilliation process for the identified org, at the remote source
    // (Using the identifier particular to that remote source)
    def result = [status:'NoJob'] // Default is no-job
    result.job = active_jobs["${internal_org_id}:${remote_source_id}:${remote_collection_id}"]
    if ( result.job ) {
      log.debug("Running job")
      // There is an active job
      result.staus = 'Active'
    }
    else {
      log.debug("No active job")
    }
  }
}
