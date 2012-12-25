#!/usr/bin/groovy

@Grapes([
  @Grab(group='com.gmongo', module='gmongo', version='0.9.2'),
  @Grab( 'org.ccil.cowan.tagsoup:tagsoup:1.2.1' )
])

import org.ccil.cowan.tagsoup.Parser


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
        geocodes++;
      }
    }

    println("processed: ${records_processed}, geocodes: ${geocodes}, cache_hits: ${gaz_cache_hits}");
  }
}
