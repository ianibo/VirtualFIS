package vfis

import com.k_int.vfis.*
import com.k_int.vfis.auth.*

import grails.plugins.springsecurity.Secured

class FrontpageController {

  def springSecurityService

  def index() { 
    def result=[:]
    if ( ( springSecurityService?.principal ) && (springSecurityService?.principal instanceof String) ) {
      log.debug("principal is string...")
    }
    else if ( springSecurityService?.principal?.id ) {
      result.user = VfisPerson.get(springSecurityService.principal.id)
    }
    result
  }
}
