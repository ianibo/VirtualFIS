package vfis

import com.k_int.vfis.*
import com.k_int.vfis.auth.*

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
    def result=[]
    log.debug("Index.....");
    log.debug("HomeController::index springSecurityService.principal=${springSecurityService.principal}");
    if ( springSecurityService.principal ) {
      if ( springSecurityService.principal instanceof String ) {
        log.debug("HomeController::index user=${springSecurityService.principal}");
      }
      else {
        result.user = VfisPerson.get(springSecurityService.principal.id)
        log.debug("HomeController::index user=${user}");
      }
    }
    else {
      log.error("Principal is null");
    }

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
    def org = Organisation.get(params.org)
    if ( ( org != null ) && ( params.role != null ) ) {
      def p = new PersonOrg(dateRequested:System.currentTimeMillis(), status:0, org:org, person:user, role:params.role)
      p.save(flush:true)
    }
    redirect(action: "memberships")
  }

}
