package vfis

import com.k_int.vfis.*
import com.k_int.vfis.auth.*

import grails.plugins.springsecurity.Secured

class ContentController {

  def springSecurityService
  def mongoService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def edit() {
    def result = [:]
    log.debug("ContentController::edit")
    def mdb = mongoService.getMongo().getDB('vfis')
    if ( ( params.id ) && ( params.id.length() > 0 ) ) {
      log.debug("Process edit")
      def rec_to_edit = mdb.content.findOne(_id:new org.bson.types.ObjectId(params.id))
      if ( rec_to_edit != null ) {
        //log.debug("Got record")
        def layout = getLayout(rec_to_edit)
        log.debug("layout: ${layout}")

        result.layout = layout
        result.record = rec_to_edit
      }
      else {
        log.error("Unable to locate record to edit for id ${params.id}")
      }
    }
    else {
      log.debug("No ID")
      redirect(controller:"home", action: "index")
    }

    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def save() {
    def mdb = mongoService.getMongo().getDB('vfis')
    // The clean parameter map from the request
    // log.debug("form params: ${request.getParameterMap()}")
    def rec_to_edit = mdb.content.findOne(_id:new org.bson.types.ObjectId(params.id))
    def changelog = []
    request.getParameterMap().each() { kvp ->
      if ( kvp.key.startsWith('src.') ) {
        def current_value = org.apache.commons.beanutils.PropertyUtils.getProperty(rec_to_edit,kvp.key)
        def value_from_page = kvp.value[0]
        log.debug("test ${kvp.key} == ${value_from_page} (orig=${current_value})")

        if ( current_value == value_from_page ) {
          // log.debug("Value unchanged")
        }
        else {
          // log.debug("Value changed")
          org.apache.commons.beanutils.PropertyUtils.setProperty(rec_to_edit, kvp.key, value_from_page)
          changelog.add([prop:kvp.key,from:current_value,to:value_from_page]);
        }
      }
      else {
        log.debug("skip ${kvp.key}")
      }
    }

    if ( changelog.size() > 0 ) {
      log.debug("Record was changed.. ${changelog}")
      flash.message = "Record updated"
      mdb.content.save(rec_to_edit)
    }

    redirect(controller:"content", action: "edit", id:params.id)
  }

  def getLayout(record) {
    // Simple for now, later on per user, customise and auto-generate for types with no layout.
    generateLayout(record)
  }

  def generateLayout(record) {
    // Create list of simple text fields

    [ 
      'type':'tabs',
      'tabs': [     // Tabs: An array of maps - 1 map for each tab.
        [
          'id' : 'basic_dets',
          'label' : 'Basic Details',
          'content' : generateControls(record.src)
        ]
      ]
    ]
  }

  def generateControls(context) {
    def result = []
    context.each { kvpair ->
      if ( kvpair.value ) {
        if ( kvpair.value instanceof Map ) {
          // log.debug("${kvpair.key} is a map")
          result.add([type:"link", 
                      display:"border", 
                      linklabel:kvpair.key, 
                      propname:kvpair.key,
                      content:generateControls(kvpair.value)])
        }
        else if ( kvpair.value instanceof List ) {
          log.debug("${kvpair.key} is a list")
        }
        else {
          // log.debug("Consider key=${kvpair.key} value=${kvpair.value} class=${kvpair.value?.class?.name}")
          result.add([type:'text', label:kvpair.key, propname:kvpair.key])
          // add helptext:'' for helptext
        }
      }
      else {
        // Null
      }
    }
    result
  }
}
