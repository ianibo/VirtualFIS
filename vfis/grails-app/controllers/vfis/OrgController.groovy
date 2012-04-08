package vfis

import com.k_int.vfis.*
import com.k_int.vfis.auth.*

import grails.plugins.springsecurity.Secured

class OrgController {

  def springSecurityService
  def reconciliationService
  def mongoService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def dashboard() { 
    def result=[:]
    result.org = Organisation.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def reconciliationStatus() { 
    def mdb = mongoService.getMongo().getDB('vfis')
    log.debug("reconciliationStatus(${params.id})");
    def result=[:]
    result.org = Organisation.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)
    result.reconciliation = reconciliationService.getStatus(params.id, 'OFS', result.org.code)

    if ( ( result.reconciliation?.active) && ( result.reconciliation.job.max ) ) {
      log.debug( "${result.reconciliation.job.start} / ${result.reconciliation.job.max} = ${result.reconciliation.job.start/result.reconciliation.job.max}" )
      result.progress = (double)( (double)(result.reconciliation.job.start) / (double)(result.reconciliation.job.max) ) * 100
    }
    else {
      result.progress = 0
    }

    def task_id = "${result.org.id}:OFS:${result.org.code}".toString()
    result.persistentInfo = mdb.reconciliationSources.find(reconSource:task_id)
    if ( result.persistentInfo )
      log.debug("got persistent info")
    else 
      log.debug("no persistent info")
   
    def pageno = params.pageno ?: 0 
    result.records = []
    mdb.reconRecords.find(reconSource:task_id).limit(10).skip(pageno*10).sort('docid':1).each { r ->
      result.records.add(r)
    }

    log.debug("Reconcole OFS for org ${result.org.name} identifier ${result.org.identifier} code ${result.org.code}")    
    result    
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def requestReconciliation() { 
    log.debug("requestReconciliation(${params.id})");
    def result=[:]
    def policy=['new_record':'auto']
    result.org = Organisation.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)
    result.reconciliation = reconciliationService.startReconciliation(params.id, 'OFS', result.org.code, policy)
    redirect(action: "reconciliationStatus", id:params.id)
  }

}
