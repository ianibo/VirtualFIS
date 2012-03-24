package vfis

import com.k_int.vfis.auth.*

import grails.plugins.springsecurity.Secured

class HomeController {

  def springSecurityService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
    log.debug("Index.....");
    log.debug("HomeController::index springSecurityService.principal=${springSecurityService.principal}");
    if ( springSecurityService.principal ) {
      if ( springSecurityService.principal instanceof String ) {
        log.debug("HomeController::index user=${springSecurityService.principal}");
      }
      else {
        def user = VfisPerson.get(springSecurityService.principal.id)
        log.debug("HomeController::index user=${user}");
      }
    }
    else {
      log.error("Principal is null");
    }
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def memberships() {
    log.debug("Memberships");
  }
}
