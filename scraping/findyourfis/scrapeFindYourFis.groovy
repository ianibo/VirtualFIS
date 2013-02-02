#!/usr/bin/groovy

@Grapes([
  @Grab(group='com.gmongo', module='gmongo', version='0.9.2'),
  @Grab( 'org.ccil.cowan.tagsoup:tagsoup:1.2.1' )
])

import org.ccil.cowan.tagsoup.Parser


println "Open mongo";
def mongo = new com.gmongo.GMongo()
def db = mongo.getDB("find_your_fis_crawl_db")

try {
  synchronized(this) {
    Thread.sleep(4000);
  }
}
catch ( Exception e ) {
}

println "Grab page...";
go(db);

mongo.close();


def go(db) {

  def next_page_url = 'http://findyourfis.daycaretrust.org.uk/kb5/findyourfis/results.page?qt=&region='
  def rcount = 0;
  def ecount = 0;

  while ( next_page_url ) {
    def gHTML = new URL( next_page_url ).withReader { r ->
      new XmlSlurper( new Parser() ).parse( r )
      // Find the table with ID Hits
    }

    // Look for  <select name="localDrp" class="form-select" id="edit-localDrp" >
    // def allLinks = gHTML.body.'**'.findAll { it.name() == 'select' && it.@id.text() == 'edit-localDrp' }
    def hits_table = gHTML.body.'**'.find { it.name() == 'table' && it.@id.text() == 'hits' }
    println("Got hits table");
    hits_table.tbody.tr.each { hits_row ->
      println("Processing hits row.....");
      def profile_td = hits_row.td[0];
      def org_name = hits_row.td[1];
      def contact_dets = hits_row.td[2];   
      println("Org name is ${org_name.a.text()}");
    }

    def next_page_link = gHTML.body.'**'.find { it.name() == 'a' && it.@title == 'Next Page' }
    println("Next page link is ${next_page_link?.@href}");
    if ( next_page_link )
      next_page_url = "${next_page_link?.@href}".toString();
    else
      next_page_url = null;
  }
}
