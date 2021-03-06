package vfis

import com.k_int.vfis.*
import com.k_int.vfis.auth.*
import com.k_int.iep.datamodel.*

import grails.plugins.springsecurity.Secured

class HomeController {

  def springSecurityService

  def addUserOrgs(user) {
    def result = []
    user.adminAssociations.each { assoc ->
      if ( assoc.status == 1 ) {
        result.add(assoc.org);
      }
    }
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
    def result=[:]
    result.user = VfisPerson.get(springSecurityService.principal.id)
    // If admin
      // Work out if there are any pending membership requests, if so, flag up as flash message on home page
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def memberships() {
    log.debug("Memberships");
    def result = [:]
    result.user = VfisPerson.get(springSecurityService.principal.id)
    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def processJoinRequest() {
    log.debug("processJoinRequest org with id ${params.org}");
    def user = VfisPerson.get(springSecurityService.principal.id)
    def org = IEPProvider.get(params.org)
    if ( ( org != null ) && ( params.role != null ) ) {
      def p = new PersonOrg(dateRequested:System.currentTimeMillis(), status:0, org:org, person:user, role:params.role)
      p.save(flush:true)
    }
    redirect(action: "memberships")
  }

}
