
import info.localchatter.auth.*


class BootStrap {

  def grailsApplication

  def init = { servletContext ->

    def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
    def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
    def fbRole = Role.findByAuthority('ROLE_FACEBOOK') ?: new Role(authority: 'ROLE_FACEBOOK').save(failOnError: true)


    log.debug("Processing bootstrap user accounts... ${grailsApplication.config.sysusers}");
    grailsApplication.config.sysusers.each { su ->
      log.debug("test ${su.name} ${su.pass} ${su.display} ${su.roles}");
      def user = User.findByUsername(su.name)
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
        user = new User(
                      username: su.name,
                      password: su.pass,
                      email: su.email,
                      enabled: true).save(failOnError: true)
      }

      log.debug("Add roles for ${su.name}");
      su.roles.each { r ->
        def role = Role.findByAuthority(r)
        if ( ! ( user.authorities.contains(role) ) ) {
          log.debug("  -> adding role ${role}");
          UserRole.create user, role
        }
        else {
          log.debug("  -> ${role} already present");
        }
      }
    }
  }


  def destroy = {
  }
}
