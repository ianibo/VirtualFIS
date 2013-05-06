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
  ['name': 'Kent', url: 'https://fisonline.tribalhosted.co.uk/Kent/EarlyYears/FSD/Search.aspx'],
  // ['name': 'TowerHamlets', url: 'https://fisonline.tribalhosted.co.uk/TowerHamlets/EarlyYears/PublicEnquiry/Search.aspx'],
  // ['name': 'centralbedfordshire', url: 'https://fisonline.tribalhosted.co.uk/centralbedfordshire/fiso/publicenquiry/Search.aspx'],
  // ['name': 'Bracknell', url: 'https://fisonline.tribalhosted.co.uk/Bracknell/EarlyYears/PublicEnquiry/Search.aspx'],
  // ['name': 'Doncaster', url: 'https://fisonline.tribalhosted.co.uk/Doncaster/EarlyYears/FamilyServiceDirectory/Search.aspx'],
  // ['name': 'Aberdeen', url: 'https://fisonline.tribalhosted.co.uk/Doncaster/EarlyYears/FamilyServiceDirectory/Search.aspx'],
  // ['name': 'Lambeth', url: 'https://fisonline.tribalhosted.co.uk/Lambeth/FISO/PublicEnquiry/Search.aspx']
]

go(db, urls);
mongo.close();

def go(db, urls) {

  urls.each { url ->
    println("Processing ${url}");

    def rcount = 0;
    def ecount = 0;
    def range = 'a'..'z'
    range.each { letter ->
      println("Letter: ${letter}");
      processLetter(url, letter)
    }

  }
}

def processLetter(url, letter) {
  def full_search_url = "${url.url}?letter=${letter}"
  println("Process ${url.name} : ${full_search_url}");
  def gHTML = new URL( full_search_url ).withReader { r ->
    new XmlSlurper( new Parser() ).parse( r )
  }
}
