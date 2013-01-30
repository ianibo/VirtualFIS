package com.k_int.lc

import grails.converters.*

class SitemapController {

  def elasticSearchService

  def index() { 

    log.debug("SitemapController::index");

     org.elasticsearch.groovy.node.GNode esnode = elasticSearchService.getESNode()
     org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

    def result = [:]
    result.providers = []
    // query_string (query: '*:*')
    try {
      log.debug("Search....");
      def search = esclient.search{
        indices "localchatter"
        types "resource"
        source {
          query {
            match_all()
          }
          facets {
            provider {
              terms {
                field = 'provider'
                all_terms = true
              }
            }
          }
        }
      }

      log.debug("Search response: ${search}");
      
      if ( search.response.facets != null ) {
        log.debug("Got facets");
        search.response.facets.facets.each { facet ->
          log.debug("Processing: ${facet}");
          facet.value.entries.each { fe ->
            result.providers.add(fe.term)
          }
        }
      }
      else {
        log.debug("No facets");
      }

    }
    catch ( Exception e ) {
      e.printStackTrace()
    }

    render result as JSON
  }
}
