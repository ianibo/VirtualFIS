package info.localchatter.admin

import com.k_int.kbplus.auth.*;
import grails.plugins.springsecurity.Secured
import grails.converters.*

class SyseditController {

  def springSecurityService

  @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
  }

  
}
