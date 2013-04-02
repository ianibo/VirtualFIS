package info.localchatter

class ShareSomethingController {

  def newGazetteerService

  def index() { 
    log.debug("ShareSomethingController::index()");
  }

  def check() { 
    flash.clear()
    log.debug("ShareSomethingController::check(${params})");

    def result=[:]

    if ( params.proposed_postcode &&
         params.proposed_name &&
         params.proposed_postcode.trim() != '' &&
         params.proposed_name.trim() != '' ) {

      // 1. validate postcode
      def postcode_info = newGazetteerService.geocode(params.proposed_postcode)
      if ( postcode_info ) {
        log.debug("Validated ${params.proposed_postcode}");
        result.proposed_name = params.proposed_name
        result.proposed_postcode = params.proposed_postcode
        render view:'describe', model:result
        return
      }
      else {
        log.debug("invalid postcode ${params.proposed_postcode}");
        flash.error = "Cannot recognise the postcode ${params.proposed_postcode}"
        render view:'index'
      }
    }
    else {
      flash.error = "Postcode and Name must be completed"
    }

    render view:'index'
  }

  def describe() {
  }

}
