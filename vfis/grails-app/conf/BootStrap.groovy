import com.k_int.vfis.auth.*

class BootStrap {

    def springSecurityService

    def init = { servletContext ->
      log.debug("BootStrap::init");
 
      log.debug("Create roles...");
      def userRole = VfisAuthority.findByAuthority('ROLE_USER') ?: new VfisAuthority(authority: 'ROLE_USER').save(failOnError: true)
      def adminRole = VfisAuthority.findByAuthority('ROLE_ADMIN') ?: new VfisAuthority(authority: 'ROLE_ADMIN').save(failOnError: true)

      log.debug("Create admin user...");
      def adminUser = VfisPerson.findByUsername('admin') ?: new VfisPerson(
                        username: 'admin',
                        password: 'admin',
                        enabled: true).save(failOnError: true)
 
      if (!adminUser.authorities.contains(adminRole)) {
        VfisPersonVfisAuthority.create adminUser, adminRole
      }

      if (!adminUser.authorities.contains(userRole)) {
        VfisPersonVfisAuthority.create adminUser, userRole
      }
    }
    def destroy = {
    }
}
