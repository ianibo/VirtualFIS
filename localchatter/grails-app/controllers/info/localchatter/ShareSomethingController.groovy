package info.localchatter

class ShareSomethingController {

  def index() { 
    log.debug("ShareSomethingController::index()");
  }

  def check() { 
    log.debug("ShareSomethingController::check(${params})");
    render view:'index'
  }
}
