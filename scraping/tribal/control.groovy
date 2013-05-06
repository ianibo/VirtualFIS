#!/home/ibbo/.gvm/groovy/current/bin/groovy

@Grapes([
  @Grab(group='com.gmongo', module='gmongo', version='0.9.2'),
  @Grab( 'org.ccil.cowan.tagsoup:tagsoup:1.2.1' )
])

import org.ccil.cowan.tagsoup.Parser


println "Open mongo";
def mongo = new com.gmongo.GMongo()
def db = mongo.getDB("ofsted_crawl_db")

def urls = [
  ['name': 'Kent', url: 'https://fisonline.tribalhosted.co.uk/Kent/EarlyYears/FSD'],
  // ['name': 'TowerHamlets', url: 'https://fisonline.tribalhosted.co.uk/TowerHamlets/EarlyYears/PublicEnquiry'],
  // ['name': 'centralbedfordshire', url: 'https://fisonline.tribalhosted.co.uk/centralbedfordshire/fiso/publicenquiry'],
  // ['name': 'Bracknell', url: 'https://fisonline.tribalhosted.co.uk/Bracknell/EarlyYears/PublicEnquiry'],
  // ['name': 'Doncaster', url: 'https://fisonline.tribalhosted.co.uk/Doncaster/EarlyYears/FamilyServiceDirectory'],
  // ['name': 'Aberdeen', url: 'https://fisonline.tribalhosted.co.uk/Doncaster/EarlyYears/FamilyServiceDirectory'],
  // ['name': 'Lambeth', url: 'https://fisonline.tribalhosted.co.uk/Lambeth/FISO/PublicEnquiry']
]

go(db, urls);
mongo.close();

def go(db, urls) {

  urls.each { url ->
    println("Processing ${url}");

    def rcount = 0;
    def ecount = 0;
    def range = ['a'] // 'a'..'z'
    range.each { letter ->
      println("Letter: ${letter}");
      processLetter(url, letter)
    }

  }
}

def processLetter(url, letter) {
  def full_search_url = "${url.url}/Search.aspx?letter=${letter}"
  println("Process ${url.name} : ${full_search_url}");
  def gHTML = new URL( full_search_url ).withReader { r ->
    new XmlSlurper( new Parser() ).parse( r )
  }

  def sel = gHTML.body.'**'.find { it.name() == 'table' && it.@id.text() == 'ctl00_ContentPlaceHolder1_GridView1' }

  def column_index = [:]
  boolean process = false
  sel.tr.each { tr ->
    println("Processing tr ${tr}");
    if ( process ) {
      def details_link_tr = tr.td[column_index['Name']]
      def name = details_link_tr.a.@title.text()
      // def details_url = "${url.url}/${details_link_tr.a.@href.text()}"; // Old way, not reliable
      def details_anchor_tag = tr.'**'.find { it.name() == 'a' && it.@href.text().startsWith('Search.aspx')}
      if ( details_anchor_tag ) {
        println("Result of find: ${details_anchor_tag}");
        def details_url = "${url.url}/${details_anchor_tag.@href.text()}"
        processRecord(url.name, details_url)
      }

    }
    else {
      process = true; // First tr is a header row, process all subsequent rows.
      int counter=0;
      tr.th.each { th ->
        column_index[th.text()] = counter++;
      }
      println("Constructed column index: ${column_index}");
    }
  }
}

def processRecord(owner, url) {
  println("Find ${url} owned by ${owner}");
}
