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

go(crawl_db, gazcache_db)

mongo.close();


def go(crawl_db, gazcache_db) {

  crawl_db.ofsted.find().each { rec ->
    def address = rec.address;
    if ( address && address.size() > 0 ) {
      def postcode = address[address.size()-1]
      println("Candidate postcode: ${postcode}");
    }
  }
}
