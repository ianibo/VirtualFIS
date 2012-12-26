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

    org.elasticsearch.groovy.common.xcontent.GXContentBuilder.rootResolveStrategy = Closure.DELEGATE_FIRST; 

    log.debug("Init");

    GNodeBuilder nodeBuilder = new org.elasticsearch.groovy.node.GNodeBuilder()

    log.debug("Construct node settings");

    nodeBuilder.settings {
      node {
        client = true
      }
      cluster {
        name = "aggr"
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
