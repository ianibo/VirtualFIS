package api

import org.elasticsearch.groovy.node.GNode
import org.elasticsearch.groovy.node.GNodeBuilder
import static org.elasticsearch.groovy.node.GNodeBuilder.*
import org.codehaus.groovy.grails.commons.ApplicationHolder


class ElasticSearchService {

  static transactional = false

  def gNode = null;

  @javax.annotation.PostConstruct
  def init() {
    log.debug("Init");
    // org.elasticsearch.groovy.common.xcontent.GXContentBuilder.rootResolveStrategy = Closure.DELEGATE_FIRST; 


    // GNodeBuilder nodeBuilder = new org.elasticsearch.groovy.node.GNodeBuilder()
    def nodeBuilder = new org.elasticsearch.groovy.node.GNodeBuilder()
    def clus_nm = ApplicationHolder.application.config.es.cluster ?: "elasticsearch"

    log.debug("Construct node settings - ES cluster name will be ${clus_nm}");

    nodeBuilder.settings {
      node {
        client = true
      }
      cluster {
        name = clus_nm
      }
      http {
        enabled = false
      }
    }

    log.debug("Constructing node...");
    gNode = nodeBuilder.node()

    log.debug("Init completed");
  }

  @javax.annotation.PreDestroy
  def destroy() {
    log.debug("Destroy");
    gNode.close()
    log.debug("Destroy completed");
  }

  def getESNode() {
    log.debug("getESNode()");
    gNode
  }

}
