package cxfisppsoap


import grails.converters.*
import grails.plugins.springsecurity.Secured
import groovy.xml.MarkupBuilder
import grails.converters.*
import java.security.MessageDigest


class RestController {

  def springSecurityService
  def depositService

  // Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
  }

  // Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def deposit() {
    log.debug("Deposit");
    if ( request.method == "POST" ) {
      def user = springSecurityService.currentUser 
      log.debug("POST: User: ${user}")
      def provider = params.owner;
      def file = request.getFile("upload")

      def record = new String(file.getBytes())
      depositService.upload(record,true,provider,user.username);
      log.debug("rest deposit post done");
    }

    log.debug("rest deposit done");

  }
}
