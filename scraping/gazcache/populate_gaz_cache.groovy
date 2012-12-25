#!/usr/bin/groovy

@Grapes([
  @Grab(group='com.gmongo', module='gmongo', version='0.9.2'),
  @Grab( 'org.ccil.cowan.tagsoup:tagsoup:1.2.1' ),
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2'),
  @Grab(group='org.apache.httpcomponents', module='httpclient', version='4.0'),
  @Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0')

])

import org.ccil.cowan.tagsoup.Parser
import static groovyx.net.http.Method.*
import groovyx.net.http.RESTClient
import groovyx.net.http.*


println "Open mongo";
def mongo = new com.gmongo.GMongo()
def crawl_db = mongo.getDB("ofsted_crawl_db")
def gazcache_db = mongo.getDB("gazcache")

try {
  go(crawl_db, gazcache_db)
}
catch ( Exception e ) {
  e.printStackTrace();
}

mongo.close();


def go(crawl_db, gazcache_db) {

  int geocodes = 0;
  int geocode_limit = 2000;
  int records_processed = 0;
  int gaz_cache_hits=0;

  crawl_db.ofsted.find().each { rec ->

    if ( geocodes >= geocode_limit ) {
      println("Hit geocode limit, returning");
      throw new RuntimeException("Max geocodes");
    }
    else {
      records_processed++;
    }

    def address = rec.address;
    if ( address && address.size() > 0 ) {
      def postcode = address[address.size()-1]
      println("Candidate postcode: ${postcode}");

      def gaz_entry = gazcache_db.entries.find(postcode:postcode) 
      if ( gaz_entry ) {
        gaz_cache_hits++;
      }
      else {
        geocode(postcode, gazcache_db);
        geocodes++;
      }
    }

    println("processed: ${records_processed}, geocodes: ${geocodes}, cache_hits: ${gaz_cache_hits}");
  }
}

def reverseGeocode(lat,lng) {

  def searches = ["administrative_area_level_2","postal_code_prefix","route","locality"]
  def geoLocation = ['county','postcode','street','locality']
  def result =[:]
  def http = new HTTPBuilder("http://maps.googleapis.com");
  http.request(Method.valueOf("GET"), JSON) {
    uri.path = '/maps/api/geocode/json'
    uri.query = [ 'latlng' : "$lat,$lng", 'sensor' : 'false' ]
    response.success = {resp, json ->
      json.results[0].address_components.each { ac ->
        def i = 0
        while (i<4){
          if ( ac.types.contains(searches[i]) ) {
            result[geoLocation[i]]="${ac.long_name}"
          }
          i++
        }
      }
    }
  }
  result
}


def geocode(postcode, gazcache_db) {

  def searches = ["administrative_area_level_2","postal_code_prefix","route","locality"]
  def geoLocation = ['county','postcode','street','locality']
  def result =[:]
  def http = new HTTPBuilder("http://maps.googleapis.com");
  http.request(Method.valueOf("GET"), ContentType.JSON) {
    uri.path = '/maps/api/geocode/json'
    uri.query = [ 'address' : "$postcode", 'sensor' : 'false' ]
    response.success = {resp, json ->

      def cache_entry = [ address:postcode,
                          response:json,
                          lastSeen: System.currentTimeMillis(),
                          created: System.currentTimeMillis() ]

      gazcache_db.entries.save(cache_entry);

      // json.results[0].address_components.each { ac ->
      //   def i = 0
      //   while (i<4){
      //     if ( ac.types.contains(searches[i]) ) {
      //       result[geoLocation[i]]="${ac.long_name}"
      //     }
      //     i++
      //   }
      // }
    }
  }
  result
}

