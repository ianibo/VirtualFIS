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

def mongo = new com.gmongo.GMongo()
def db = mongo.getDB("find_your_fis_crawl_db")

System.in.withReader {
  print 'localchatter pass:'
  rest_upload_pass = it.readLine()
}
  
go(db,rest_upload_pass)

// println 'Grab page...'
// go(db, '887');

mongo.close();

def go(db, rest_upload_pass) {
  db.fis.find().each{ rec ->
    makeRecord(rec);
  }
}

// Construct records according to  http://schema.org/GovernmentOffice
def makeRecord(rec) {
  def upload_record = [:]
  upload_record.'$schema'="http://schema.org/GovernmentOffice"
  upload_record.url=rec.'Web site'
  upload_record.name=rec.name
  upload_record.image=rec.logoUrl
  upload_record.address=[:]
  upload_record.address.email=rec.Email
  upload_record.address.telephone=rec.Helpline
  upload_record.address.streetAddress=rec.Address
  upload_record.address.postalCode=rec.Postcode

  println("Created upload record ${upload_record}");
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
