package api

import static groovyx.net.http.Method.*
import groovyx.net.http.RESTClient
import groovyx.net.http.*

class NewGazetteerService {

  // Alternate geocoding from http://uk-postcodes.com/api.php
  def mongoService
  def geocode_count = 0

  def geocode(address) {
    def gazcache_db = mongoService.getMongo().getDB("osgazcache")
    def geo_result = gazcache_db.entries.findOne(address:address)
    if ( !geo_result ) {
      log.debug("No cache hit for ${address}, lookup");
      if ( geocode_count < 2500 ) {
        geo_result = codepointOpenGeocode(address, gazcache_db);
      }
    }
    else {
      log.error("Unable to geocode ${address}");
    }

    def result = geo_result.response.results[0]

    result
  }

  def codepointOpenGeocode(postcode, gazcache_db) {

    def http = new HTTPBuilder("http://uk-postcodes.com/postcode/S119DE");
    http.request(Method.valueOf("GET"), ContentType.JSON) {
      uri.path = '/postcode/${postcode}.json'
      // uri.query = [ 'address' : "$postcode", 'sensor' : 'false' ]
      response.success = {resp, json ->

        def cache_entry = [ address:postcode,
                            response:json,
                            lastSeen: System.currentTimeMillis(),
                            created: System.currentTimeMillis() ]
  
        gazcache_db.entries.save(cache_entry);
        geocode_count++;
      }
    }
    result
  }
}
