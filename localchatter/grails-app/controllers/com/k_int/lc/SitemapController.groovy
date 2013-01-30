package com.k_int.lc

import grails.converters.*
import groovy.xml.MarkupBuilder

class SitemapController {

  def elasticSearchService

  def index() { 

    log.debug("SitemapController::index");

    org.elasticsearch.groovy.node.GNode esnode = elasticSearchService.getESNode()
    org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

    def formatter = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    def default_date = formatter.format(new Date(System.currentTimeMillis()))

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
            result.providers.add([name:fe.term,count:fe.count,lastModified:getLastModified(fe.term,esclient)])
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

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    xml.sitemapindex('xmlns:xsd':'http://www.w3.org/2001/XMLSchema',
                     'xmlns:xsi':'http://www.w3.org/2001/XMLSchema-instance',
                     'xsi:schemaLocation':'http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd',
                     'xmlns':'http://www.sitemaps.org/schemas/sitemap/0.9',
                     'targetNamespace':'http://www.sitemaps.org/schemas/sitemap/0.9') {
      result.providers.each { prov ->
        sitemap() {
          loc("${grailsApplication.config.frontend}/sitemap/provider/${prov.name}")
          lastmod( prov.lastModified != null ? prov.lastModified : default_date );
          mkp.comment("Doc count for this authority: ${prov.count}")
        }
      }
    }

    println "Render...siteindex"

    render(contentType:'application/xml', text: writer.toString())
  }

  def getLastModified(provid,esclient) {
    log.debug("YAY")
    def formatter = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    def default_date = formatter.format(new Date(System.currentTimeMillis()))

    def search = esclient.search{
      indices "localchatter"
      types "resource"
      source {
        query {
          query_string (query: "provider:${provid}")
        }
        sort = [
                  'lastModified' : [
                    'order' : 'asc',
                  ]
        ]
      }
    }

    default_date
  }

  def provider() {
  }
}
