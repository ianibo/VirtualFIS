package cxfisppsoap

import org.grails.cxf.soap.*

import org.grails.cxf.utils.EndpointType
import javax.jws.WebService
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext

// http://comments.gmane.org/gmane.comp.apache.cxf.user/21060

@WebService(endpointInterface='org.grails.cxf.soap.ISPPPortType',
            name = 'DepositEndpoint',
            targetNamespace = 'http://dcsf.gov.uk/ISPP/Webservice',
            serviceName = 'ISPPDepositImplService',
            portName = 'ISPPPortTypePort')
class DepositEndpoint {

  def depositService

  @Resource
  private WebServiceContext wsContext;

  static expose = EndpointType.JAX_WS_WSDL
  static wsdl = 'resources/deposit.wsdl'

  public UploadResponseT upload( UploadRequestT doc) {

    java.security.Principal pr = wsContext.getUserPrincipal();

    log.debug("upload endpoint ${wsContext} - principal is ${pr}");

    // MessageContext ctx = (MessageContext) context.getMessageContext(); 
    // List recv = (List)ctx.get("RECV_RESULTS"); 
    // WSHandlerResult wsResult = (WSHandlerResult)recv.get(0); 
    // WSSecurityEngineResult wsseResult = (WSSecurityEngineResult)wsResult.getResults().get(0); 
    // String login = wsseResult.getPrincipal().getName(); 
    // System.out.println("login = " + login); 

    depositService.upload(doc.doc, doc.authoritative, doc.owner, pr?.getName());

    UploadResponseT resp = new UploadResponseT()
    resp.status="OK"
    resp.addinfo=""
    resp.location=""

    println("Got request ${doc} sending response ${resp}");
    return resp
  }

}
