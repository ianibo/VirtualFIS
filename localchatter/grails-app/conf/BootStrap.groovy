
import info.localchatter.auth.*
import org.springframework.core.io.Resource
import org.codehaus.groovy.grails.commons.ApplicationAttributes
import grails.util.GrailsUtil

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

    bootstrapTypeSystem(servletContext);
  }


  def destroy = {
  }

  def bootstrapTypeSystem(servletContext) {

    log.debug("Loading bootstrap type definitions from disk cache");

    def ctx = servletContext.getAttribute(ApplicationAttributes.APPLICATION_CONTEXT)
    Resource r = ctx.getResource("/WEB-INF/basetypes");
    def f = r.getFile();
    log.debug("got types dir: ${f}");

    // see http://groovy.dzone.com/news/class-loading-fun-groovy for info on the strategy being used here

    if ( f.isDirectory() ) {
      f.listFiles().each { type_declaration ->
      }
    }
  }
}
