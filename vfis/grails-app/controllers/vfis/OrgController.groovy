package vfis

import com.k_int.vfis.*
import com.k_int.vfis.auth.*
import com.k_int.iep.datamodel.*

import grails.plugins.springsecurity.Secured

class OrgController {

  def springSecurityService
  def reconciliationService
  def mongoService

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def dashboard() { 
    def result=[:]
    result.org = IEPProvider.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)

    log.debug("Looking up feedback records for authority with identifier ${result.org.identifier}")

    result.feedback = IEPResourceMessage.findAll("from IEPResourceMessage m where m.owner.owner=? and m.status=? order by m.messageTimeStamp desc",
                                                     ['open',result.org],[max:20]);

    log.debug(result.feedback)

    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def feedback() {
    def result=[:]
    result.org = IEPProvider.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)

    log.debug("Looking up feedback records for authority with identifier ${result.org.identifier}")

    result.feedback = IEPResourceMessage.findAll("from IEPResourceMessage m where m.owner.owner=? and m.status=? order by m.messageTimeStamp desc",
                                                     ['open',result.org],[max:20]);

    log.debug(result.feedback)

    result
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def issue() {
    def result=[:]
    result.org = IEPProvider.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)

    redirect(action:'feedback',id:params.id)
  }


  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def reconciliationStatus() { 
    def mdb = mongoService.getMongo().getDB('vfis')
    log.debug("reconciliationStatus(${params.id})");
    def result=[:]
    result.org = IEPProvider.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)
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
    result    
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def requestReconciliation() { 
    log.debug("requestReconciliation(${params.id})");
    def result=[:]
    def policy=['new_record':'auto']
    result.org = IEPProvider.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)
    result.reconciliation = reconciliationService.startReconciliation(params.id, 'OFS', result.org.code, policy)
    redirect(action: "reconciliationStatus", id:params.id)
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def search() {
    def mdb = mongoService.getMongo().getDB('vfis')
    def result=[:]
    result.hpp = params.hpp ? integer.parseInt(params.hpp) : 10
    result.org = IEPProvider.get(params.id);
    result.user = VfisPerson.get(springSecurityService.principal.id)
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
    result
  }
}
