import com.k_int.vfis.auth.*
import com.k_int.vfis.*

import grails.util.GrailsUtil

class BootStrap {

    def springSecurityService
    def dataSyncService

    def init = { servletContext ->
      log.debug("BootStrap::init");
 
      log.debug("Create roles...");
      def userRole = VfisAuthority.findByAuthority('ROLE_USER') ?: new VfisAuthority(authority: 'ROLE_USER').save(failOnError: true)
      def adminRole = VfisAuthority.findByAuthority('ROLE_ADMIN') ?: new VfisAuthority(authority: 'ROLE_ADMIN').save(failOnError: true)

      log.debug("Create admin user...");
      def adminUser = VfisPerson.findByUsername('admin')
      if ( ! adminUser ) {
        def newpass = java.util.UUID.randomUUID().toString()
        log.error("No admin user found, create with temporary password ${newpass}")
        adminUser = new VfisPerson(
                        username: 'admin',
                        password: 'admin',
                        email: 'admin@localhost',
                        enabled: true).save(failOnError: true)
      }
 
      if (!adminUser.authorities.contains(adminRole)) {
        VfisPersonVfisAuthority.create adminUser, adminRole
      }

      if (!adminUser.authorities.contains(userRole)) {
        VfisPersonVfisAuthority.create adminUser, userRole
      }


      switch (GrailsUtil.environment) {
        case "development":
          log.debug("Dev env setup");
          // dataSyncService.sync();
          break
        case "live":
          log.debug("Live env setup");
          // dataSyncService.sync();
          break
        case "test":
          log.debug("Test env setup");
          break
        default:
          break
      }
    }


    def destroy = {
    }
}
