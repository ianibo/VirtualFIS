package info.localchatter.admin

import com.k_int.kbplus.auth.*;
import grails.plugins.springsecurity.Secured
import grails.converters.*

class SyseditController {

  def springSecurityService
  def mongoService

  @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
  def index() { 
    def result = [:]

    def mdb = mongoService.mongo.getDB(params.database)
    def oid = new org.bson.types.ObjectId(params.id)
    log.debug("Looking for object with _id ${params.id} in colleciton ${params.collection} from db ${params.database}");

    def o = mdb."${params.collection}".findOne(_id:oid)

    log.debug("Got object ${o}");

    //result.typeInfo = mongoTypeService.identify(o)
    //result.layoutInfo = templateServie.generate

    result    
  }

  
}
