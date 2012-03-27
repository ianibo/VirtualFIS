package vfis

import com.k_int.vfis.*
import com.k_int.vfis.auth.*
import grails.plugins.springsecurity.Secured

class AdminController {

  def springSecurityService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def index() {
  }

  @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
  def membershipRequests() {
    log.debug("Memberships");
    def result = [:]
    result.user = VfisPerson.get(springSecurityService.principal.id)

    // List all pending requests...
    result.pendingRequests = PersonOrg.findAllByStatus(0, [sort:'dateRequested'])
    result
  }


  @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
  def actionMembershipRequest() {
    log.debug("actionMembershipRequest");
    def req = PersonOrg.get(params.req);
    def user = VfisPerson.get(springSecurityService.principal.id)
    if ( req != null ) {
      switch(params.act) {
        case 'approve':
          req.status = 1;
          break;
        case 'deny':
          req.status = 2;
          break;
        default:
          log.error("FLASH UNKNOWN CODE");
          break;
      }
      req.actionedBy = user
      req.dateActioned = System.currentTimeMillis();
      req.save(flush:true);
    }
    else {
      log.error("FLASH");
    }
    redirect(action: "membershipRequests")
  }
}
