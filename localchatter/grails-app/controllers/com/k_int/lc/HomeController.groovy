package com.k_int.lc

class HomeController {

  def index() { 
    if ( params.postcode || params.keyword ) {
      render(view:'results');
    }
    else {
      render(view:'index');
    }
  }
}
