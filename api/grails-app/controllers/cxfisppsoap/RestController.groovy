package cxfisppsoap


import grails.converters.*
import grails.plugins.springsecurity.Secured
import groovy.xml.MarkupBuilder

class RestController {

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def deposit() {
    log.debug("Deposit");
  }

}
