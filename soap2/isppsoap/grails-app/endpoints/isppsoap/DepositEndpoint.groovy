package isppsoap



class DepositEndpoint {
	
  def static namespace = "http://dcsf.gov.uk/ISPP/Webservice/encodedTypes"

  def invoke = { request, response ->
    log.debug("Incoming deposit request ${request}");

    response.HolidayResponse(xmlns: namespace) {
      status('complete') 
    }
  }

}
