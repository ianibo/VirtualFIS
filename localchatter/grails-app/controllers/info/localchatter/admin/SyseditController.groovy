package info.localchatter.admin

import com.k_int.kbplus.auth.*;
import grails.plugins.springsecurity.Secured
import grails.converters.*

class SyseditController {

  def springSecurityService
  def mongoService
  def mongoTypeService
  def layoutService

  @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
    def result = [:]

    def mdb = mongoService.mongo.getDB(params.database)
    def oid = new org.bson.types.ObjectId(params.id)
    log.debug("Looking for object with _id ${params.id} in colleciton ${params.collection} from db ${params.database}");

    def o = mdb."${params.collection}".findOne(_id:oid)

    log.debug("Got object ${o}");

    if ( o ) {
      result.o = o;
      def type_info = mongoTypeService.extractTypeInfoFor(o);
      log.debug("Type info: ${type_info}");

      if (params.type) {
        log.debug("Get layout for type ${params.type}")
      }
      else {
        // No type specified
        log.debug("Lookup layout for first type: ${type_info[0].name}");
        result.layoutInfo = layoutService.generateDefaultLayout(type_info[0], o);
        render view:'templateDisplay', model:result
      }
    }

    //result.typeInfo = mongoTypeService.identify(o)
    //result.layoutInfo = templateServie.generate

    result    
  }

  
}
