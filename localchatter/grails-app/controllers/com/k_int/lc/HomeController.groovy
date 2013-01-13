package com.k_int.lc

class HomeController {

  def newGazetteerService

  def index() { 
    log.debug("index");
    if ( params.postcode || params.keyword ) {
      if ( params.postcode ) {
        log.debug("Geocode ${params.postcode}"); 
        def place = newGazetteerService.geocode(params.postcode.toUpperCase().trim())
        if ( place == null ) {
          flash.error = "Unable to look up postcode ${params.postcode}, please try again with a different one"
        }
        log.debug("Place:${place}");
      }
      render(view:'results');
    }
    else {
      render(view:'index');
    }
  }
}
