// import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor
// import org.apache.ws.security.handler.WSHandlerConstants

import vf.*;

class BootStrap {

  def depositEndpointFactory

  def init = { servletContext ->

      def userRole = VFRole.findByAuthority('ROLE_USER') ?: new VFRole(authority: 'ROLE_USER').save(failOnError: true)
      def adminRole = VFRole.findByAuthority('ROLE_ADMIN') ?: new VFRole(authority: 'ROLE_ADMIN').save(failOnError: true)

      def adminUser = VFUser.findByUsername('admin')
      if ( ! adminUser ) {
        // def newpass = java.util.UUID.randomUUID().toString()
        // log.error("No admin user found, create with temporary password ${newpass}")
        adminUser = new VFUser(
                        username: 'test',
                        password: 'tset',
                        email: 'admin@localhost',
                        enabled: true).save(failOnError: true)
      }

      if (!adminUser.authorities.contains(adminRole)) {
        VFUserRole.create adminUser, adminRole
      }

      if (!adminUser.authorities.contains(userRole)) {
        VFUserRole.create adminUser, userRole
      }

  }

  def destroy = {
  }
}
