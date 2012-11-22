package isppsoap



import org.codehaus.groovy.grails.plugins.spring.ws.EndpointFunctionalTestCase

class DepositEndpointFunctionalTests extends EndpointFunctionalTestCase {
	
  def serviceURL = "http://localhost:8080/isppsoap/services"

  def namespace = "http://dcsf.gov.uk/ISPP/Webservice/encodedTypes"

  void setUp(){
    super.setUp()
    webServiceTemplate.setDefaultUri(serviceURL)
  }
    
  void testSOAPDocumentService() {
    println("testing soap service");
    def response = withEndpointRequest(serviceURL) {
      uploadRequest(xmlns: namespace) {
        doc("doc")
        authoritative(true)
        owner("owner")
      }
    }

    log.info("Completed");
    println(response)
  }

}
