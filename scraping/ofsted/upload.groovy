#!/home/ibbo/.gvm/groovy/current/bin/groovy

@Grapes([
  @Grab(group='com.gmongo', module='gmongo', version='1.1'),
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2'),
  @Grab(group='org.apache.httpcomponents', module='httpclient', version='4.0'),
  @Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0'),
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2')
])

import groovy.util.slurpersupport.GPathResult
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.*
import java.nio.charset.Charset
import org.apache.http.*
import org.apache.http.protocol.*

def mongo = new com.gmongo.GMongo()
def db = mongo.getDB("ofsted_crawl_db")

def codes_to_process = [ 301, 302, 370, 800, 822, 303, 330, 889, 890, 350, 837, 867, 380, 304, 846, 801, 305, 825, 351, 381, 873, 202, 823, 895, 896, 201, 908, 331, 306, 909, 841, 831, 830, 878, 371, 835, 332, 840, 307, 811, 845, 308, 881, 390, 916, 203, 204, 876, 205, 850, 309, 310, 805, 311, 884, 919, 312, 313, 921, 420, 206, 207, 886, 810, 314, 382, 340, 208, 888, 383, 856, 855, 209, 925, 341, 821, 352, 887, 315, 806, 826, 391, 316, 926, 812, 813, 802, 392, 815, 928, 929, 892, 891, 353, 931, 874, 879, 836, 851, 870, 317, 807, 318, 354, 372, 857, 355, 333, 343, 373, 893, 871, 334, 933, 803, 393, 852, 882, 210, 342, 860, 356, 808, 861, 935, 394, 936, 319, 866, 357, 894, 883, 880, 211, 358, 384, 335, 320, 212, 877, 937, 869, 938, 213, 359, 868, 344, 872, 336, 885, 816 ];

def snac_lookups = [ [ofstedcode:"380",name:"Bradford",snac:"00CX"], [ofstedcode:"304",name:"Brent",snac:"00AE"], [ofstedcode:"846",name:"Brighton and Hove",snac:"00ML"], [ofstedcode:"801",name:"Bristol",snac:"00HB"], [ofstedcode:"305",name:"Bromley",snac:"00AF"], [ofstedcode:"825",name:"Buckinghamshire",snac:"0011"], [ofstedcode:"351",name:"Bury",snac:"00BM"], [ofstedcode:"381",name:"Calderdale",snac:"00CY"], [ofstedcode:"873",name:"Cambridgeshire",snac:"0012"], [ofstedcode:"202",name:"Camden",snac:"00AG"], [ofstedcode:"823",name:"Central Bedfordshire",snac:"009A"], [ofstedcode:"895",name:"Cheshire East",snac:"013B"], [ofstedcode:"896",name:"Cheshire West and Chester",snac:"013A"], [ofstedcode:"201",name:"City of London",snac:"00AA"], [ofstedcode:"908",name:"Cornwall",snac:"0015"], [ofstedcode:"331",name:"Coventry",snac:"00CQ"], [ofstedcode:"306",name:"Croydon",snac:"00AH"], [ofstedcode:"909",name:"Cumbria",snac:"0016"], [ofstedcode:"841",name:"Darlington",snac:"00EH"], [ofstedcode:"831",name:"Derby",snac:"00FK"], [ofstedcode:"830",name:"Derbyshire",snac:"0017"], [ofstedcode:"878",name:"Devon",snac:"0018"], [ofstedcode:"371",name:"Doncaster",snac:"00CE"], [ofstedcode:"835",name:"Dorset",snac:"0019"], [ofstedcode:"332",name:"Dudley",snac:"00CR"], [ofstedcode:"840",name:"Durham",snac:"0020"], [ofstedcode:"307",name:"Ealing",snac:"00AJ"], [ofstedcode:"811",name:"East Riding of Yorkshire",snac:"00FB"], [ofstedcode:"845",name:"East Sussex",snac:"0021"], [ofstedcode:"308",name:"Enfield",snac:"00AK"], [ofstedcode:"881",name:"Essex",snac:"0022"], [ofstedcode:"390",name:"Gateshead",snac:"00CH"], [ofstedcode:"916",name:"Gloucestershire",snac:"0023"], [ofstedcode:"203",name:"Greenwich",snac:"00AL"], [ofstedcode:"204",name:"Hackney",snac:"00AM"], [ofstedcode:"876",name:"Halton",snac:"00ET"], [ofstedcode:"205",name:"Hammersmith and Fulham",snac:"00AN"], [ofstedcode:"850",name:"Hampshire",snac:"0024"], [ofstedcode:"309",name:"Haringey",snac:"00AP"], [ofstedcode:"310",name:"Harrow",snac:"00AQ"], [ofstedcode:"805",name:"Hartlepool",snac:"00EB"], [ofstedcode:"311",name:"Havering",snac:"00AR"], [ofstedcode:"884",name:"Herefordshire",snac:"00GA"], [ofstedcode:"919",name:"Hertfordshire",snac:"0026"], [ofstedcode:"312",name:"Hillingdon",snac:"00AS"], [ofstedcode:"313",name:"Hounslow",snac:"00AT"], [ofstedcode:"921",name:"Isle of Wight",snac:"00MW"], [ofstedcode:"420",name:"Isles of Scilly",snac:"15UH"], [ofstedcode:"206",name:"Islington",snac:"00AU"], [ofstedcode:"207",name:"Kensington and Chelsea",snac:"00AW"], [ofstedcode:"886",name:"Kent",snac:"0029"], [ofstedcode:"810",name:"Kingston upon Hull",snac:"00FA"], [ofstedcode:"314",name:"Kingston upon Thames",snac:"00AX"], [ofstedcode:"382",name:"Kirklees",snac:"00CZ"], [ofstedcode:"340",name:"Knowsley",snac:"00BX"], [ofstedcode:"208",name:"Lambeth",snac:"00AY"], [ofstedcode:"888",name:"Lancashire",snac:"0030"], [ofstedcode:"383",name:"Leeds",snac:"00DA"], [ofstedcode:"856",name:"Leicester",snac:"00FN"], [ofstedcode:"302",name:"Barnet",snac:"00AC"], [ofstedcode:"822",name:"Bedford",snac:"0009"], [ofstedcode:"890",name:"Blackpool",snac:"00EY"], [ofstedcode:"350",name:"Bolton",snac:"00BL"], [ofstedcode:"855",name:"Leicestershire",snac:"0031"], [ofstedcode:"209",name:"Lewisham",snac:"00AZ"], [ofstedcode:"925",name:"Lincolnshire",snac:"0032"], [ofstedcode:"341",name:"Liverpool",snac:"00BY"], [ofstedcode:"821",name:"Luton",snac:"00KA"], [ofstedcode:"352",name:"Manchester",snac:"00BN"], [ofstedcode:"887",name:"Medway",snac:"00LC"], [ofstedcode:"315",name:"Merton",snac:"00BA"], [ofstedcode:"806",name:"Middlesbrough",snac:"00EC"], [ofstedcode:"826",name:"Milton Keynes",snac:"00MG"], [ofstedcode:"391",name:"Newcastle upon Tyne",snac:"00CJ"], [ofstedcode:"316",name:"Newham",snac:"00BB"], [ofstedcode:"926",name:"Norfolk",snac:"0033"], [ofstedcode:"812",name:"North East Lincolnshire",snac:"00FC"], [ofstedcode:"813",name:"North Lincolnshire",snac:"00FD"], [ofstedcode:"802",name:"North Somerset",snac:"00HC"], [ofstedcode:"392",name:"North Tyneside",snac:"00CK"], [ofstedcode:"815",name:"North Yorkshire",snac:"0036"], [ofstedcode:"928",name:"Northamptonshire",snac:"34UF"], [ofstedcode:"929",name:"Northumberland",snac:"0035"], [ofstedcode:"892",name:"Nottingham",snac:"00FY"], [ofstedcode:"891",name:"Nottinghamshire",snac:"0037"], [ofstedcode:"353",name:"Oldham",snac:"00BP"], [ofstedcode:"931",name:"Oxfordshire",snac:"0038"], [ofstedcode:"874",name:"Peterborough",snac:"00JA"], [ofstedcode:"879",name:"Plymouth",snac:"00HG"], [ofstedcode:"836",name:"Poole",snac:"00HP"], [ofstedcode:"851",name:"Portsmouth",snac:"00MR"], [ofstedcode:"870",name:"Reading",snac:"00MC"], [ofstedcode:"317",name:"Redbridge",snac:"00BC"], [ofstedcode:"807",name:"Redcar and Cleveland",snac:"00EE"], [ofstedcode:"318",name:"Richmond upon Thames",snac:"00BD"], [ofstedcode:"354",name:"Rochdale",snac:"00BQ"], [ofstedcode:"372",name:"Rotherham",snac:"00CF"], [ofstedcode:"857",name:"Rutland",snac:"00FP"], [ofstedcode:"355",name:"Salford",snac:"00BR"], [ofstedcode:"333",name:"Sandwell",snac:"00CS"], [ofstedcode:"343",name:"Sefton",snac:"00CA"], [ofstedcode:"373",name:"Sheffield",snac:"00CG"], [ofstedcode:"893",name:"Shropshire",snac:"0039"], [ofstedcode:"871",name:"Slough",snac:"00MD"], [ofstedcode:"334",name:"Solihull",snac:"00CT"], [ofstedcode:"933",name:"Somerset",snac:"0040"], [ofstedcode:"803",name:"South Gloucestershire",snac:"00HD"], [ofstedcode:"393",name:"South Tyneside",snac:"00CL"], [ofstedcode:"852",name:"Southampton",snac:"00MS"], [ofstedcode:"882",name:"Southend on Sea",snac:"00KF"], [ofstedcode:"210",name:"Southwark",snac:"00BE"], [ofstedcode:"342",name:"St Helens",snac:"00BZ"], [ofstedcode:"860",name:"Staffordshire",snac:"0041"], [ofstedcode:"356",name:"Stockport",snac:"00BS"], [ofstedcode:"808",name:"Stockton-on-Tees",snac:"00EF"], [ofstedcode:"861",name:"Stoke-on-Trent",snac:"00GL"], [ofstedcode:"935",name:"Suffolk",snac:"0042"], [ofstedcode:"394",name:"Sunderland",snac:"00CM"], [ofstedcode:"936",name:"Surrey",snac:"0043"], [ofstedcode:"319",name:"Sutton",snac:"00BF"], [ofstedcode:"866",name:"Swindon",snac:"00HX"], [ofstedcode:"357",name:"Tameside",snac:"00BT"], [ofstedcode:"894",name:"Telford and Wrekin",snac:"00GF"], [ofstedcode:"883",name:"Thurrock",snac:"00KG"], [ofstedcode:"880",name:"Torbay",snac:"00HH"], [ofstedcode:"211",name:"Tower Hamlets",snac:"00BG"], [ofstedcode:"358",name:"Trafford",snac:"00BU"], [ofstedcode:"384",name:"Wakefield",snac:"00DB"], [ofstedcode:"335",name:"Walsall",snac:"00CU"], [ofstedcode:"320",name:"Waltham Forest",snac:"00BH"], [ofstedcode:"212",name:"Wandsworth",snac:"00BJ"], [ofstedcode:"877",name:"Warrington",snac:"00EU"], [ofstedcode:"937",name:"Warwickshire",snac:"0044"], [ofstedcode:"869",name:"West Berkshire",snac:"00MB"], [ofstedcode:"938",name:"West Sussex",snac:"0045"], [ofstedcode:"213",name:"Westminster",snac:"00BK"], [ofstedcode:"359",name:"Wigan",snac:"00BW"], [ofstedcode:"865",name:"Wiltshire",snac:"0046"], [ofstedcode:"868",name:"Windsor and Maidenhead",snac:"00ME"], [ofstedcode:"344",name:"Wirral",snac:"00CB"], [ofstedcode:"872",name:"Wokingham",snac:"00MF"], [ofstedcode:"336",name:"Wolverhampton",snac:"00CW"], [ofstedcode:"885",name:"Worcestershire",snac:"0047"], [ofstedcode:"816",name:"York",snac:"00FF"], [ofstedcode:"301",name:"Barking and Dagenham",snac:"00AB"], [ofstedcode:"370",name:"Barnsley",snac:"00CC"], [ofstedcode:"800",name:"Bath and North East Somerset",snac:"00HA"], [ofstedcode:"303",name:"Bexley",snac:"00AD"], [ofstedcode:"330",name:"Birmingham",snac:"00CN"], [ofstedcode:"889",name:"Blackburn with Darwen",snac:"00EX"], [ofstedcode:"837",name:"Bournemouth",snac:"00HN"], [ofstedcode:"867",name:"Bracknell Forest",snac:"00MA"] ];

def snac_map = [:]

if ( args.length != 1 ) {
  println("Usgae: ./upload.groovy <URL>\nExample urls \"http://localhost:8080\", \"http://api.localchatter.info\"");
  System.exit(1);
}

snac_lookups.each { it ->
  snac_map[it.ofstedcode] = it;
}

def rest_upload_pass = ""
System.in.withReader {
  print 'ofs pass:'
  rest_upload_pass = it.readLine()
}


  
codes_to_process.each { code ->
  println("Process ${code} - uploading to ${args[0]}");
  go(db,rest_upload_pass,"${code}", snac_map, args[0]);
}

// println 'Grab page...'
// go(db, '887');

mongo.close();

def go(db, rest_upload_pass, authcode, snac_map, url) {
  def max_batch_size = 10000;
  def maxts = db.config.findOne(propname:"${authcode}-maxts")

  def snac_info = snac_map[authcode]

  if ( snac_info && snac_info.snac ) {

    println("Inside go for ${authcode}");

    if ( maxts == null ) {
      println("Create new tracking config for this authority");
      maxts = [ propname:"${authcode}-maxts".toString(), value:0 ]
      db.config.save(maxts);
    }
    else {
      println("Using existing config value...");
      // In testing, reprocess evey time
      maxts.value = 0;
      db.config.save(maxts);
    }

    // def dpp = new RESTClient('http://localhost:8080/api/rest/deposit')
    // def api = new RESTClient('http://localhost:8080')
    def api = new RESTClient(url)

    // Add preemtive auth
    api.client.addRequestInterceptor( new HttpRequestInterceptor() {
      void process(HttpRequest httpRequest, HttpContext httpContext) {
        String auth = "admin:${rest_upload_pass}"
        String enc_auth = auth.bytes.encodeBase64().toString()
        httpRequest.addHeader('Authorization', 'Basic ' + enc_auth);
      }
    })

    // dpp.auth.basic 'test', 'tset'

    def ctr = 0;
    db.ofsted.find( [ lastModified : [ $gt : maxts.value ], authority:authcode ] ).sort(lastModified:1).limit(max_batch_size).each { rec ->
      maxts.value = rec.lastModified
      def ecdrec = genecd(rec);
      // if ( !alreadyPresent(rec.ofstedId,dpp)) {
        post(ecdrec,api,rec,snac_info.snac);
      // }
      println("processed[${ctr++}] records for code ${authcode}, maxts.value updated to ${rec.lastModified}");
    }
    println("processed ${ctr} records for authcode ${authcode}");
    println("Updating maxts ${maxts} for ${authcode}");
    db.config.save(maxts);
  }
}

def genecd(rec) {
  println("posting record ${rec}");
  def writer = new StringWriter()
  def xml = new groovy.xml.MarkupBuilder(writer)
  xml.setOmitEmptyAttributes(true);
  xml.setOmitNullAttributes(true);

  def formatter = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  //def default_date = formatter.format(new Date(System.currentTimeMillis()))
  def last_mod_date = new Date(rec.lastModified)

  xml.'ProviderDescription'(
                      'xsi:schemaLocation':'http://dcsf.gov.uk/XMLSchema/Childcare http://www.ispp-aggregator.org.uk/f1les/ISPP/docs/schemas/v5/ProviderTypes-v1-1e.xsd',
                      'xmlns' : 'http://dcsf.gov.uk/XMLSchema/Childcare',
                      'xmlns:lg' : 'http://www.esd.org.uk/standards',
                      'xmlns:gms': 'http://www.govtalk.gov.uk/CM/gms',
                      'xmlns:n2' : 'http://www.govtalk.gov.uk/CM/gms-xs',
                      'xmlns:core'  :'http://www.govtalk.gov.uk/core',
                      'xmlns:n3' : 'http://www.govtalk.gov.uk/metadata/egms',
                      'xmlns:con' : 'http://www.govtalk.gov.uk/people/ContactTypes',
                      'xmlns:apd' : 'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails',
                      'xmlns:bs7666' : 'http://www.govtalk.gov.uk/people/bs7666',
                      'xmlns:xsi' : 'http://www.w3.org/2001/XMLSchema-instance') {
    'DC.Title'(rec.name)
    'DC.Identifier'(rec.uri)
    'Description' {
      'DC.DESCRIPTION'(format:'plain',rec.name)
    }
    'DCTerms'('Childcare')
    // 'DC.Subject'('Childcare')
    'DC.Creator'('OFSTED')
    'DC.Publisher'('href':rec.uri,'OFSTED')
    // 'DC.Date.Created'('title')
    'DC.Date.Modified'(formatter.format(last_mod_date))
    'ProviderDetails' {
      'ProviderName'(rec.name)
      'ConsentVisibleAddress'(true)
      'SettingDetails' {
        'TelephoneNumber' {
          if ( rec.contact.size() > 0 ) {
            if ( ( rec.contact[0].startsWith('Telephone number: ') ) && ( rec.contact[0].length() > 18 ) ) {
              'apd:TelNationalNumber'(rec.contact[0].substring(18))
            }
            else {
              'apd:TelNationalNumber'(rec.contact[0])
            }
          }
        }
        'PostalAddress' {
          'apd:A_5LineAddress' {
            rec.address.each { addrline ->
              'apd:Line'(addrline)
            }
            'apd:PostCode'(rec.postcode)
          }
        }
      }
      'ChildcareType'('Childcare on non-domestic premises') // Creche,...
      'ProvisionType'() // CCD/CCN,
      'ChildcareAges'() // CCN,
      'Country'('United Kingdom') // United Kingdom
      'ModificationDate'(formatter.format(last_mod_date))
      'RegistrationDetails'(RegistrationId:rec.ofstedId) {
        'RegistrationDate'(rec.regdate)
        'RegistrationConditions'()
        'RegistrationTypes' {
          'RegistrationType'() // VCR
        }
        'RegistrationStatus' {
          'RegistrationStatus'('ACTV') // ACTV
          'RegistrationStatusStartDate'(rec.regdate) // YYYY-MM-DD
        }
        if ( rec.reports.size() > 0 ) {
          'LastInspection' {
            'InspectionType'()
            'InspectionDate'()  // yyyy-mm-dd
            'InspectionOverallJudgementDescription'()
          }
        }
        'WelfareNoticeHistoryList'()
      }
      'LinkedRegistration'{
      }
      'QualityAssurance' {
        'QualityLevel' {
          'QualityStatus'(Id:1,ItemName:'Unknown',ListName:'QualityAssurance-1.0','Unknown')
        }
      }
      'FromOfsted'('true')
      'OfstedURN'(rec.ofstedId)
    }
  }

  def result = writer.toString();
  result;
}


def post(rec, 
         target_service, 
         orig_rec, 
         authority) {

  byte[] rec_as_bytes = rec.getBytes('UTF-8')
  println("Attempting post... [${rec_as_bytes.length}] to ${target_service}");
  try {
    target_service.request(POST) { request ->
      requestContentType = 'multipart/form-data'
      uri.path='/api/rest/deposit'
      def multipart_entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
      multipart_entity.addPart("owner", new StringBody( 'ofsted', 'text/plain', Charset.forName('UTF-8')))
      def uploaded_file_body_part = new org.apache.http.entity.mime.content.ByteArrayBody(rec_as_bytes, 'text/xml', 'filename.xml')
      multipart_entity.addPart("upload", uploaded_file_body_part);

      request.entity = multipart_entity

      response.success = { resp, data ->
        println("OK - Record uploaded. Authority:${authority}, URI:${orig_rec.uri}")
      }

      response.failure = { resp ->
        println("Error - ${resp.status}. Authority:${authority}, URI:${orig_rec.uri}");
        System.out << resp
        println("Done\n\n");
      }
    }
  }
  catch ( Exception e ) {
    e.printStackTrace();
  }
}

def alreadyPresent(ofstedcode, target_service) {
  // http://aggregator.openfamilyservices.org.uk/index/aggr/select?q=ofsted_urn_s:300808&fl=dc.title,ofsted_urn_s,dc.identifier&wt=json
  def result = true;
  println("Checking if ${ofstedcode} already present");
  try {
    target_service.request(GET, ContentType.JSON) { request ->
      uri.path='index/aggr/select'
      uri.query = [
        q:"ofsted_urn_s:${ofstedcode}",
        fl:'dc.title,ofsted_urn_s,dc.identifier,aggr.internal.id',
        wt:'json'
      ]
      response.success = { resp, data ->
        if ( data.response.numFound == 0 ) {
          println("Record ${ofstedcode} not present in service.. uploading!");
          result = false
        }
        else {
          println("Record ${ofstedcode} already present (${data.response.numFound} times) Not uploading.");
        }
      }
      response.failure = { resp ->
        println("Error - ${resp.status}");
        System.out << resp
      }
    }
  }
  catch ( Exception e ) {
    e.printStackTrace();
  }
  result
}
