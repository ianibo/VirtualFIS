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
  def reconcileOfs() { 
    def result=[:]
    result.org = Organisation.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)
    result.reconciliation = reconciliationService.getStatus(params.id, 'OFS', 'result.org.code')

    log.debug("Reconcole OFS for org ${result.org.name} identifier ${result.org.identifier} code ${result.org.code}")    
    result    
  }

}
