#!/home/ibbo/.gvm/groovy/current/bin/groovy

@Grapes([
  @Grab(group='com.gmongo', module='gmongo', version='1.1'),
  @Grab( 'org.ccil.cowan.tagsoup:tagsoup:1.2.1' ),
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2'),
  @Grab(group='org.apache.httpcomponents', module='httpclient', version='4.0'),
  @Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0')
])

import org.ccil.cowan.tagsoup.Parser
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

println "Open mongo";
def mongo = new com.gmongo.GMongo()
def db = mongo.getDB("tribal_crawl_db")

def urls = [
  ['name': 'Kent', url: 'https://fisonline.tribalhosted.co.uk/Kent/EarlyYears/FSD'],
  // ['name': 'TowerHamlets', url: 'https://fisonline.tribalhosted.co.uk/TowerHamlets/EarlyYears/PublicEnquiry'],
  // ['name': 'centralbedfordshire', url: 'https://fisonline.tribalhosted.co.uk/centralbedfordshire/fiso/publicenquiry'],
  // ['name': 'Bracknell', url: 'https://fisonline.tribalhosted.co.uk/Bracknell/EarlyYears/PublicEnquiry'],
  // ['name': 'Doncaster', url: 'https://fisonline.tribalhosted.co.uk/Doncaster/EarlyYears/FamilyServiceDirectory'],
  // ['name': 'Aberdeen', url: 'https://fisonline.tribalhosted.co.uk/Doncaster/EarlyYears/FamilyServiceDirectory'],
  // ['name': 'Lambeth', url: 'https://fisonline.tribalhosted.co.uk/Lambeth/FISO/PublicEnquiry']
]

go(db, urls);
mongo.close();

def go(db, urls) {

  urls.each { url ->
    println("Processing ${url}");

    def rcount = 0;
    def ecount = 0;
    // def range = 'a'..'z'
    def range = 'w'..'z'
    range.each { letter ->
      println("Letter: ${letter}");
      processLetter(url, letter, db)
    }

  }
}

def processLetter(url, letter, db) {
  def full_search_url = "${url.url}/Search.aspx?letter=${letter}"
  println("Process ${url.name} : ${full_search_url}");
  def pageno = 0;

  def cookies = []

  new HTTPBuilder( full_search_url ).with {
    get(contentType:TEXT) { resp, reader ->
      gHTML = new XmlSlurper( new Parser() ).parse( reader )
      resp.getHeaders('Set-Cookie').each {
        String cookie = it.value.split(';')[0]
        println("Adding cookie to collection: $cookie")
        cookies.add(cookie)
      }
    }
  }

  // def gHTML = new URL( full_search_url ).withReader { r ->
  //   new XmlSlurper( new Parser() ).parse( r )
  // }

  println("Processing page ${pageno}");
  def process_result = processResponsePage(gHTML, url,db);

  println("Will set cookies header to ${cookies.join(';')}");
  while ( process_result.has_next ) {

    println("Processing page ${++pageno}");

    // We have to do a form post to the URL, sucks ass majorly, but there you go
    new HTTPBuilder( full_search_url ).with {
      request(POST,TEXT) {

        // def viewstate = new String(process_result.viewstate_value.decodeBase64())
        def viewstate = process_result.viewstate_value

        headers.'Content-Type' = 'application/x-www-form-urlencoded'
        requestContentType = URLENC


        body=['__VIEWSTATE':viewstate,
              "${process_result.pagination_name}":'Next',
              '__EVENTVALIDATION':process_result.event_validation
        ]

        headers['Cookie'] = cookies.join(';')


        response.success = { resp, reader ->
          println("Got page....");
          def gHTML2 = new XmlSlurper( new Parser() ).parse( reader )
          process_result = processResponsePage(gHTML2, url, db);
        }
      }
    }
  }

}

// Read a remote page, process any elements, return VIEWSTATE and a boolean indicating if there is another page

def processResponsePage(gHTML,url,db) {

  def result = [:]

  def viewstate_element = gHTML.body.'**'.find { it.name() == 'input'  && it.@id.text() == 'VIEWSTATE' }
  def viewstate_value = null;
  // This is severely FUGLY code on the server side
  if ( viewstate_element ) {
    result.viewstate_value = viewstate_element.@value.text();
  }

  def next_button_element = gHTML.body.'**'.find { it.name() == 'input'  && it.@value.text() == 'Next' && it.@class.text() == 'paging_next_button'}
  
  if ( next_button_element == null ) {
    println("No next page");
    result.has_next = false
    result.pagination_name = null;
  }
  else if ( next_button_element.@disabled == 'disabled' ) {
    println("No next page");
    result.has_next = false
    result.pagination_name = null;
  }
  else {
    println("Has next page");
    result.has_next = true
    result.pagination_name = next_button_element.@name
    println("Pagination element will be ${result.pagination_name}");
  }

  def event_validation_element = gHTML.body.'**'.find { it.name() == 'input'  && it.@name.text() == '__EVENTVALIDATION'}
  if ( event_validation_element ) {
    result.event_validation = event_validation_element.@value.text()
  }


  def sel = gHTML.body.'**'.find { it.name() == 'table' && it.@id.text() == 'ctl00_ContentPlaceHolder1_GridView1' }
  def column_index = [:]
  boolean process = false
  sel.tr.each { tr ->
    if ( process ) {
      def details_link_tr = tr.td[column_index['Name']]
      def name = details_link_tr.a.@title.text()
      // def details_url = "${url.url}/${details_link_tr.a.@href.text()}"; // Old way, not reliable
      def details_anchor_tag = tr.'**'.find { it.name() == 'a' && it.@href.text().startsWith('Search.aspx')}
      if ( details_anchor_tag ) {
        def details_url = "${url.url}/${details_anchor_tag.@href.text()}"
        processRecord(url.name, details_url, db)
      }
    }
    else {
      process = true; // First tr is a header row, process all subsequent rows.
      int counter=0;
      tr.th.each { th ->
        column_index[th.text()] = counter++;
      }
      println("Constructed column index: ${column_index}");
    }
  }

  result
}

def processRecord(owner, url, db) {
  println("Find ${url} owned by ${owner}");

  // Get url
  // Extract all provider_result_section divs.
  // Read 
  // <div class="provider_result_section"><h3 class="provider_result_header">Additional Information</h3><div class="provider_result_content">Please contact provider by post, email or telephone.<br />Venue to be confirmed.</div></div>

  def gHTML = null
  new HTTPBuilder( url ).with {
    get(contentType:TEXT) { resp, reader ->
      gHTML = new XmlSlurper( new Parser() ).parse( reader )
    }
  }

  if ( gHTML != null ) {

    def props = [:]

    gHTML.body.depthFirst().collect { it }.findAll { it.name() == 'span' && it.@class=='provider_name_header' }.each { pn ->
      props.'name' = pn
    }

    gHTML.body.depthFirst().collect { it }.findAll { it.name() == 'span' && it.@class=='provider_type_header' }.each { pt ->
      props.'type' = pt
    }

    gHTML.body.depthFirst().collect { it }.findAll { it.name() == 'div' && it.@class.text() == 'provider_result_section' }.each { p ->
      props[p.h3.text()] = p.div
    }

    def record = [:]
    addIfPresent(record, 'uri', url)
    addIfPresent(record, 'owner', owner)
    addIfPresent(record, 'name', props.name.text())
    addIfPresent(record, 'type', props.type.text())
    addIfPresent(record, 'telephone', props.Telephone?.text())
    addIfPresent(record, 'website', props.Website?.a.@href.text())
    addIfPresent(record, 'description', props.'Service Description'?.text())
    addIfPresent(record, 'ageRange', props.'Age Range Suitability'?.text())
    addIfPresent(record, 'eligibilityCriteria', props.'Eligibility Criteria'?.text())
    addIfPresent(record, 'accreditedBy', props.'Accredited By'?.text())
    addIfPresent(record, 'availability', props.'Availability'?.text())
    addIfPresent(record, 'dailyOpeningTimes', props.'Daily Opening Times'?.text())
    addIfPresent(record, 'openingTimes', props.'Opening Times'?.text())
    addIfPresent(record, 'costs', props.'Costs'?.text())
    addIfPresent(record, 'otherCosts', props.'Other Costs'?.text())
    addIfPresent(record, 'facilities', props.'Facilities'?.text())
    addIfPresent(record, 'eligibilityCriteria', props.'Eligibility Criteria'?.text())
    addIfPresent(record, 'ofstedReport', props.'OfSTED Inspection Report (If Applicable)'?.text())
    addIfPresent(record, 'languages', props.'Languages Spoken'?.text())
    addIfPresent(record, 'address', props.'Address'?.text())
    addIfPresent(record, 'mapTitle', props.'Map'?.a.@title.text())
    addIfPresent(record, 'mapLink', props.'Map'?.a.@href.text())
    record.lastSeen=System.currentTimeMillis()

    println("Rec: ${record}");

    def existing_record = db.tribalrecs.findOne(uri:url);
    if ( existing_record == null ) {
      db.tribal.save(record);
    }
    else {
      existing_record.lastSeen=System.currentTimeMillis()
      db.tribal.save(authority_info);
    }

  }

}

def addIfPresent(rec, name, value) {
  if ( ( value ) &&
       ( value.trim() != '' ) &&
       ( value.trim() != '-' ) ) {
    rec[name] = value;
  }
}
