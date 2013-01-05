package api

import static groovyx.net.http.Method.*
import groovyx.net.http.RESTClient
import groovyx.net.http.*

class GazetteerService {

  def mongoService

  def geocode(address) {
    def gazcache_db = mongoService.getMongo().getDB("gazcache")
    result = gazcache_db.entries.find(address:postcode)
    if ( !result ) {
      log.debug("No cache hit for ${address}, lookup");
      googleGeocode(address, gazcache_db);
    }

    result
  }

  def googleGeocode(postcode, gazcache_db) {

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
      }
    }
    result
  }
}
