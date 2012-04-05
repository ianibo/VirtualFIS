package vfis

import com.k_int.vfis.*
import com.k_int.vfis.auth.*

import grails.plugins.springsecurity.Secured

class OrgController {

  def springSecurityService
  def reconciliationService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def dashboard() { 
    def result=[:]
    result.org = Organisation.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def reconciliationStatus() { 
    log.debug("reconciliationStatus(${params.id})");
    def result=[:]
    result.org = Organisation.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)
    result.reconciliation = reconciliationService.getStatus(params.id, 'OFS', result.org.code)
    log.debug("Reconcole OFS for org ${result.org.name} identifier ${result.org.identifier} code ${result.org.code}")    
    result    
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def requestReconciliation() { 
    log.debug("requestReconciliation(${params.id})");
    def result=[:]
    result.org = Organisation.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)
    result.reconciliation = reconciliationService.startReconciliation(params.id, 'OFS', result.org.code)
    redirect(action: "reconciliationStatus", id:params.id)
  }

}
