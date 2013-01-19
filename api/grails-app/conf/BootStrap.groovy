// import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor
// import org.apache.ws.security.handler.WSHandlerConstants

import vf.*;

class BootStrap {

  def depositEndpointFactory
  def grailsApplication 

  def init = { servletContext ->

      def userRole = VFRole.findByAuthority('ROLE_USER') ?: new VFRole(authority: 'ROLE_USER').save(failOnError: true)
      def adminRole = VFRole.findByAuthority('ROLE_ADMIN') ?: new VFRole(authority: 'ROLE_ADMIN').save(failOnError: true)


      log.debug("Processing bootstrap user accounts... ${grailsApplication.config.sysusers}");
      grailsApplication.config.sysusers.each { su ->
        log.debug("test ${su.name} ${su.pass} ${su.display} ${su.roles}");
        def user = VFUser.findByUsername(su.name)
        if ( user ) {
          if ( user.password != su.pass ) {
            log.debug("Hard change of user password from config ${user.password} -> ${su.pass}");
            user.password = su.pass;
            user.save(failOnError: true)
          }
          else {
            log.debug("${su.name} present and correct");
          }
        }
        else {
          log.debug("Create user...");
          user = new VFUser(
                        username: su.name,
                        password: su.pass,
                        email: su.email,
                        enabled: true).save(failOnError: true)
        }

        log.debug("Add roles for ${su.name}");
        su.roles.each { r ->
          def role = VFRole.findByAuthority(r)
          if ( ! ( user.authorities.contains(role) ) ) {
            log.debug("  -> adding role ${role}");
            VFUserRole.create user, role
          }
          else {
            log.debug("  -> ${role} already present");
          }
        }
      }


      // def adminUser = VFUser.findByUsername('admin')
      // if ( ! adminUser ) {
        // def newpass = java.util.UUID.randomUUID().toString()
        // log.error("No admin user found, create with temporary password ${newpass}")
      //   adminUser = new VFUser(
      //                   username: 'admin',
      //                   password: 'changeme',
      //                   email: 'admin@localhost',
      //                   enabled: true).save(failOnError: true)
      // }

      // if (!adminUser.authorities.contains(adminRole)) {
      //   VFUserRole.create adminUser, adminRole
      // }

      // if (!adminUser.authorities.contains(userRole)) {
      //   VFUserRole.create adminUser, userRole
      // }

  }

  def destroy = {
  }
}
