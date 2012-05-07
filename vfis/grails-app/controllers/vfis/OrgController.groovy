package vfis

import com.k_int.vfis.*
import com.k_int.vfis.auth.*
import com.k_int.iep.datamodel.*

import grails.plugins.springsecurity.Secured

class OrgController {

  def springSecurityService
  def reconciliationService
  def mongoService

  def hasAccess(user,org) {
    def result = false;

    // Find out if there is an approved person org record for the current user for this org
    def po = PersonOrg.findAll("from PersonOrg po where po.org=? and po.person=? and po.status=1",[org,user])

    if ( po.size > 0 )
      result = true;

    result;
  }


  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def dashboard() { 
    def result=[:]
    result.org = IEPProvider.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)

    if ( hasAccess(result.user, result.org) ) {
      result.feedback = IEPResourceMessage.findAll("from IEPResourceMessage m where m.owner.owner=? and m.status=? order by m.messageTimeStamp desc",
                                                     [result.org,'open'],[max:20]);

    }
    else {
      log.error("org::dashboard - No access")
      flash.error="You do not have permission to view or manage ${result.org.name}. Please use this form to request access"
      redirect(controller:'home', action:'memberships',params:[req:result.org.id])
    }

    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def feedback() {
    def result=[:]
    result.org = IEPProvider.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)

    log.debug("Looking up feedback records for authority with identifier ${result.org.identifier}")

    if ( hasAccess(result.user, result.org) ) {
      result.feedback = IEPResourceMessage.findAll("from IEPResourceMessage m where m.owner.owner=? and m.status=? order by m.messageTimeStamp desc",
                                                     [result.org,'open'],[:]);

    }
    else {
      log.error("org::feedback - No access")
      flash.error="You do not have permission to view or manage ${result.org.name}. Please use this form to request access"
      redirect(controller:'home', action:'memberships',params:[req:result.org.id])
    }

    log.debug(result.feedback)

    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def issue() {
    def result=[:]

    result.org = IEPProvider.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)

    if ( hasAccess(result.user, result.org) ) {
      // update issue
    }
    else {
      log.error("org::reconcilliationStatus - No access")
      flash.error="You do not have permission to view or manage ${result.org.name}. Please use this form to request access"
      redirect(controller:'home', action:'memberships',params:[req:result.org.id])
    }

    redirect(action:'feedback',id:params.id)
  }


  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def reconciliationStatus() { 
    def result=[:]

    result.org = IEPProvider.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)

    if ( hasAccess(result.user, result.org) ) {
      log.debug("Looking up feedback records for authority with identifier ${result.org.identifier}")

      def mdb = mongoService.getMongo().getDB('vfis')
      log.debug("reconciliationStatus(${params.id})");
      result.reconciliation = reconciliationService.getStatus(params.id, 'OFS', result.org.code)

      if ( ( result.reconciliation?.active) && ( result.reconciliation.job.max ) ) {
        log.debug( "${result.reconciliation.job.start} / ${result.reconciliation.job.max} = ${result.reconciliation.job.start/result.reconciliation.job.max}" )
        result.progress = (double)( (double)(result.reconciliation.job.start) / (double)(result.reconciliation.job.max) ) * 100
      }
      else {
        result.progress = 0
      }

      def task_id = "${result.org.id}:OFS:${result.org.code}".toString()
      result.persistentInfo = mdb.reconciliationSources.find(reconSource:task_id)
      if ( result.persistentInfo )
        log.debug("got persistent info")
      else 
        log.debug("no persistent info")
     
      result.pageno = params.pageno ? Integer.parseInt(params.pageno) : 0 
      result.records = []
      mdb.reconRecords.find(reconSource:task_id).limit(10).skip(result.pageno*10).sort('docid':1).each { r ->
        result.records.add(r)
      }

      result.hitcount = mdb.reconRecords.find(reconSource:task_id).count()
      result.maxpages = (int) ( ( result.hitcount + 9 ) / 10 )
      result.pagstart = ( result.pageno - 5 > 0 ) ? result.pageno - 5 : 0;
      result.pagend = ( result.pageno + 5 > result.maxpages ) ? result.maxpages : result.pageno+5;

      log.debug("Reconcole OFS for org ${result.org.name} identifier ${result.org.identifier} code ${result.org.code}")    
    }
    else {
      log.error("org::reconcilliationStatus - No access")
      flash.error="You do not have permission to view or manage ${result.org.name}. Please use this form to request access"
      redirect(controller:'home', action:'memberships',params:[req:result.org.id])
    }

    result    
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def requestReconciliation() { 
    log.debug("requestReconciliation(${params.id})");
    def result=[:]

    result.org = IEPProvider.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)

    if ( hasAccess(result.user, result.org) ) {
      def policy=['new_record':'auto']
      result.reconciliation = reconciliationService.startReconciliation(params.id, 'OFS', result.org.code, policy)
      redirect(action: "reconciliationStatus", id:params.id)
    }
    else {
      log.error("org::requestReconcilliation - No access")
      flash.error="You do not have permission to view or manage ${result.org.name}. Please use this form to request access"
      redirect(controller:'home', action:'memberships',params:[req:result.org.id])
    }

  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def search() {

    def result=[:]

    result.org = IEPProvider.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)

    if ( hasAccess(result.user, result.org) ) {

      def mdb = mongoService.getMongo().getDB('vfis')
      result.hpp = params.hpp ? integer.parseInt(params.hpp) : 10
      result.pageno = params.pageno ? Integer.parseInt(params.pageno) : 0 
      result.records = []

      def query_params = [owner:params.id]

      if ( ( params.q ) &&
           ( params.q.length() > 0 ) ) {
        // db.content.find({'src.DC\.Title' : /.*Woking.*/}); = emulate this from mongo console
        // db.content.find({'src.DC\.Title' : /.*Woking.*/, owner:"1"}).size();
        query_params."src.DC_Title" = ~/(?i)${params.q}/
        // query_params.'type' = ~/${params.q}/ -- Works
        // query_params.src = [ type : ~/${params.q}/ ]
        // query_params.src = [ 'DC.Title' : ~/${params.q}/ ]
        // query_params.'src.ProviderDetails.OfstedURN' = 'EY317028'
        // query_params.'type' = ~/${params.q}/
     }

     log.debug("Search with params: ${query_params}")

      mdb.content.find(query_params).limit(result.hpp).skip(result.pageno*result.hpp).sort('docid':1).each { r ->
        result.records.add(r)
      }

      result.hitcount = mdb.content.find(query_params).count()
      result.maxpages = (int) ( ( result.hitcount + (result.hpp-1) ) / result.hpp )
      result.pagstart = ( result.pageno - 5 > 0 ) ? result.pageno - 5 : 0;
      result.pagend = ( result.pageno + 5 > result.maxpages ) ? result.maxpages : result.pageno+5;
   
      log.debug("Complete")
    }
    else {
      log.error("org::search - No access")
      flash.error="You do not have permission to view or manage ${result.org.name}. Please use this form to request access"
      redirect(controller:'home', action:'memberships',params:[req:result.org.id])
    }

    result
  }


  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def details() { 

    def result=[:]

    result.org = IEPProvider.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)

    if ( hasAccess(result.user, result.org) ) {
      switch (request.method) {
        case 'GET':
          if (!result.org) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'org.label', default: 'Org'), params.id])
            redirect action: 'list'
            return
          }

          break

        case 'POST':
          log.debug("Process POST ${params}");
          if (!result.org) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'org.label', default: 'Org'), params.id])
            redirect action: 'list'
            return
          }

          if (params.version) {
            def version = params.version.toLong()
            if (result.org.version > version) {
              result.org.errors.rejectValue('version', 'default.optimistic.locking.failure',
                                    [message(code: 'org.label', default: 'Org')] as Object[],
                                    "Another user has updated this Org while you were editing")
              render view: 'details', model: result
              return
            }
          }
  
          log.debug("Merge properties from form... ${params}");
          result.org.properties = params
  
          if (!result.org.save(flush: true)) {
            render view: 'details', model: result
            return
          }
          else {
            log.debug("Problesm");
            result.org.errors.each { err ->
              log.error(err);
            }
          }
  
          flash.message = message(code: 'default.updated.message', args: [message(code: 'result.org.label', default: 'Org'), org.id])
          redirect action: 'details', id: result.org.id
          break
      }
    }
    else {
      log.error("org::dashboard - No access")
      flash.error="You do not have permission to view or manage ${result.org.name}. Please use this form to request access"
      redirect(controller:'home', action:'memberships',params:[req:result.org.id])
    }
    result
  }

}
