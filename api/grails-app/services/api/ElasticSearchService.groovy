package api

import org.elasticsearch.groovy.node.GNode
import org.elasticsearch.groovy.node.GNodeBuilder
import static org.elasticsearch.groovy.node.GNodeBuilder.*

class ElasticSearchService {

  static transactional = false

  def gNode = null;

  @javax.annotation.PostConstruct
  def init() {
    log.debug("Init");

    def nodeBuilder = new org.elasticsearch.groovy.node.GNodeBuilder()

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
      // discovery {
      //   zen {
      //     minimum_master_nodes=1
      //     ping {
      //       unicast {
      //         hosts = [ "localhost" ] 
      //       }
      //     }
      //   }
      // }
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

  def getNode() {
    log.debug("getNode()");
    gNode
  }

}
