#!/usr/bin/groovy

@Grapes([
  @Grab(group='com.gmongo', module='gmongo', version='0.9.2'),
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
import net.sf.json.*

def mongo = new com.gmongo.GMongo()
def db = mongo.getDB("find_your_fis_crawl_db")
def rest_upload_pass

System.in.withReader {
  print 'localchatter pass:'
  rest_upload_pass = it.readLine()
}
  
def lcendpoint = new RESTClient('http://api.localchatter.info')
// def lcendpoint = new RESTClient('http://localhost:8080')

// Add preemtive auth
lcendpoint.client.addRequestInterceptor( new HttpRequestInterceptor() {
  void process(HttpRequest httpRequest, HttpContext httpContext) {
    String auth = "admin:${rest_upload_pass}"
    String enc_auth = auth.bytes.encodeBase64().toString()
    httpRequest.addHeader('Authorization', 'Basic ' + enc_auth);
  }
})


go(db,lcendpoint)

// println 'Grab page...'
// go(db, '887');

mongo.close();

def go(db, lcendpoint) {
  db.fis.find().each{ rec ->
    makeRecord(rec,lcendpoint);
  }
}

// Construct records according to  http://schema.org/GovernmentOffice
def makeRecord(rec,lcendpoint) {
  def upload_record = [:]
  upload_record.'$schema'="http://schema.org/GovernmentOffice"
  upload_record.url=rec.'Web site'
  upload_record.name=rec.name
  upload_record.description=rec.name
  upload_record.privacyLevel="PublicListing"
  upload_record.image=rec.logoUrl
  upload_record.address=[:]
  upload_record.address.email=rec.Email
  upload_record.address.telephone=rec.Helpline
  upload_record.address.streetAddress=rec.Address
  upload_record.address.postalCode=rec.Postcode
  upload_record.infotypes = ['families','families/childcare','localgov','localgov/services']
  upload_record.attribution = []
  upload_record.attribution.add([copiedOn:System.currentTimeMillis(), name:'Daycare Trust', url:'http://www.daycaretrust.org.uk/'])

  def upload_record_json = upload_record as JSONObject
  println("Created upload record ${upload_record_json.toString()}");
  post(upload_record_json.toString(),lcendpoint,'lcsystem', upload_record);
}

def post(rec, target_service, authority, upload_record) {

  byte[] rec_as_bytes = rec.getBytes('UTF-8')
  println("Attempting post... [${rec_as_bytes.length}] to ${target_service}");
  try {
    target_service.request(POST) { request ->
      requestContentType = 'multipart/form-data'
      uri.path='/api/rest/deposit'
      def multipart_entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
      multipart_entity.addPart("owner", new StringBody( authority, 'text/plain', Charset.forName('UTF-8')))
      def uploaded_file_body_part = new org.apache.http.entity.mime.content.ByteArrayBody(rec_as_bytes, 'application/json', 'filename.json')
      multipart_entity.addPart("upload", uploaded_file_body_part);

      request.entity = multipart_entity

      response.success = { resp, data ->
        println("OK - Record uploaded. Authority:${authority} - id is ${upload_record.url}");
      }

      response.failure = { resp ->
        println("Error - ${resp.status}. Authority:${authority} - id is ${upload_record.url}");
        System.out << resp
        println("Done\n\n");
      }
    }
  }
  catch ( Exception e ) {
    e.printStackTrace();
  }
}
