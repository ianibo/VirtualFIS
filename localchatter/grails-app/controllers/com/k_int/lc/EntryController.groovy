package com.k_int.lc

class EntryController {

  def newGazetteerService
  def elasticSearchService

  def show() { 
    def result = [:]
    if ( params.id ) {
      org.elasticsearch.groovy.node.GNode esnode = elasticSearchService.getESNode()
      org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

      def search = esclient.search {
        indices "localchatter"
        types "resource"
        source {
          query {
            term(shortcode: params.id.toString())
          }
        }
      }

      log.debug("Search returned ${search.response.hits.totalHits}")
      if ( search.response.hits.totalHits == 1 ) {
        result.record = search.response.hits.getAt(0)
      }
      else {
        redirect(controller:'home')
      }
    }
    result
  }
}
