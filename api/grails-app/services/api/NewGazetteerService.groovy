package api

import static groovyx.net.http.Method.*
import groovyx.net.http.RESTClient
import groovyx.net.http.*

class NewGazetteerService {

  // Alternate geocoding from http://uk-postcodes.com/api.php
  def mongoService

  def geocode(address) {
    def gazcache_db = mongoService.getMongo().getDB("osgazcache")
    def geo_result = gazcache_db.entries.findOne(address:address)
    if ( !geo_result ) {
      log.debug("No cache hit for ${address}, lookup");
      geo_result = codepointOpenGeocode(address, gazcache_db);
    }
    else {
      log.debug("geocode cache hit for ${address}");
    }

    geo_result
  }

  def codepointOpenGeocode(postcode, gazcache_db) {
    log.debug("looking up \"${postcode}\"");
    def result = null
    def http = new HTTPBuilder("http://uk-postcodes.com");
    http.request(Method.valueOf("GET"), ContentType.JSON) {
      uri.path = "/postcode/${postcode}.json"
      response.success = {resp, json ->
        log.debug("Process geocode response: ${json}");
        result = [ address:postcode,
                            response:json,
                            lastSeen: System.currentTimeMillis(),
                            created: System.currentTimeMillis() ]
        gazcache_db.entries.save(result);
      }
      response.failure = { resp ->
        log.debug("Not found: ${postcode}");
      }
    }
    result
  }
}
