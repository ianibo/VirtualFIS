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
      def adminUser = VfisPerson.findByUsername('admin') ?: new VfisPerson(
                        username: 'admin',
                        password: 'admin',
                        email: 'admin@localhost',
                        enabled: true).save(failOnError: true)
 
      if (!adminUser.authorities.contains(adminRole)) {
        VfisPersonVfisAuthority.create adminUser, adminRole
      }

      if (!adminUser.authorities.contains(userRole)) {
        VfisPersonVfisAuthority.create adminUser, userRole
      }


      switch (GrailsUtil.environment) {
        case "development":
          log.debug("Dev env setup");
          // def o1 = new Organisation(identifier:"Test Organisation One", code:"TestOrg1", email:"a@b.c.d",name:"Test Org 1").save();
          // def o2 = new Organisation(identifier:"Test Organisation Two", code:"TestOrg2", email:"b@b.c.d",name:"Test Org 2").save();
          // def o3 = new Organisation(identifier:"Test Organisation Three", code:"TestOrg3", email:"c@b.c.d",name:"Test Org 3").save();
          // def o4 = new Organisation(identifier:"Test Organisation Four", code:"TestOrg4", email:"d@b.c.d",name:"Test Org 4").save();
          // def o5 = new Organisation(identifier:"Test Organisation Five", code:"TestOrg5", email:"e@b.c.d",name:"Test Org 5").save();
          dataSyncService.sync();
          break
        case "live":
          log.debug("Live env setup");
          dataSyncService.sync();
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
