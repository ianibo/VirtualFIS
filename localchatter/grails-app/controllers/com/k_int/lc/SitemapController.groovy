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
            outcode {
              terms {
                field = 'outcode'
                all_terms = true
                size = 1000
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
          loc("${grailsApplication.config.frontend}/sitemap/outcode/${prov.name}")
          lastmod( prov.lastModified != null ? prov.lastModified : default_date );
          mkp.comment("Doc count for this authority: ${prov.count}")
        }
      }
    }

    println "Render...siteindex"

    render(contentType:'application/xml', text: writer.toString())
  }

  def getLastModified(outcode,esclient) {
    log.debug("getLastModified");
    def formatter = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    def default_date = formatter.format(new Date(System.currentTimeMillis()))

    def search = esclient.search{
      indices "localchatter"
      types "resource"
      source {
        query {
          query_string (query: "outcode:${outcode}")
        }
        sort = [
                  'lastModified' : [
                    'order' : 'desc',
                  ]
        ]
      }
    }
  
    log.debug(search.response.hits)
    log.debug("get last modified for ${outcode} : hits: ${search.response.hits.class.name}")

    if ( search.response.hits && ( search.response.hits.hits.length > 0 ) ) {
      def first = search.response.hits.getAt(0);
      default_date = first.source.lastModified
    }
    else {
      log.debug("No hits... default");
    }
    
    log.debug("Returning last modified of ${default_date}");
    default_date
  }

  def outcode() {

    org.elasticsearch.groovy.node.GNode esnode = elasticSearchService.getESNode()
    org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

    def formatter = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    def default_date = formatter.format(new Date(System.currentTimeMillis()))

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    try {
      log.debug("Search.... provider:${params.id}");
      def search = esclient.search{
        indices "localchatter"
        types "resource"
        source {
          from = 0
          size = 100
          query {
            query_string (query: "outcode:${params.id}")
          }
        }
      }

      int offset = 0;
      int remaining = search.response.hits.totalHits;
      int reamining_in_batch = search.response.hits.hits.length

      xml.urlset('xmlns:xsd':'http://www.w3.org/2001/XMLSchema',
                 'xmlns:xsi':'http://www.w3.org/2001/XMLSchema-instance',
                 'xsi:schemaLocation':'http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd',
                 'xmlns':'http://www.sitemaps.org/schemas/sitemap/0.9',
                 'targetNamespace':'http://www.sitemaps.org/schemas/sitemap/0.9') {
        // for provider home page
        // url() {
        //   loc("${grailsApplication.config.ofs.frontend}/ofs/directory/${params.authority}")
        //   lastmod(default_date)
        // }

        while ( remaining > 0 ) {
          
          search.response.hits.hits.each { rec ->
            url() {
              loc("${grailsApplication.config.frontend}/entry/${rec.source.shortcode}")
              lastmod(rec.source.lastModified ?: default_date )
              //changefreq('hello')
              //priority('hello')
            }
            offset++
            remaining--;
            reamining_in_batch--;
            if ( ( reamining_in_batch==0 ) && (remaining>0)) {
              // fetch another page of results
              search = esclient.search{
                      indices "localchatter"
                      types "resource"
                      source {
                        from = offset
                        size = 100
                        query {
                          query_string (query: "outcode:${params.id}")
                        }
                      }
                    }
            }
          }
        }
      }
    }
    catch ( Exception e ) {
      log.error("Problem",e)
    }

    render(contentType:'application/xml', text: writer.toString())
  }
}
