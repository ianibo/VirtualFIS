package vfis


import com.k_int.vfis.*
import com.k_int.vfis.auth.*

import grails.plugins.springsecurity.Secured

class OrgController {

  def springSecurityService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def dashboard() { 
    def result=[:]
    result.org = Organisation.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)
    result
  }
}
