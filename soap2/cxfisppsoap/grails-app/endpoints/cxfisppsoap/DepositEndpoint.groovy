package cxfisppsoap

import org.grails.cxf.soap.*

import org.grails.cxf.utils.EndpointType
import javax.jws.WebService

@WebService(endpointInterface='org.grails.cxf.soap.ISPPPortType',
            name = 'DepositEndpoint',
            targetNamespace = 'http://dcsf.gov.uk/ISPP/Webservice',
            serviceName = 'ISPPDepositImplService',
            portName = 'ISPPPortTypePort')
class DepositEndpoint {

  def depositService

  static expose = EndpointType.JAX_WS_WSDL
  static wsdl = 'resources/deposit.wsdl'

  public UploadResponseT upload( UploadRequestT doc) {

    log.debug("upload endpoint");

    // MessageContext ctx = (MessageContext) context.getMessageContext(); 
    // List recv = (List)ctx.get("RECV_RESULTS"); 
    // WSHandlerResult wsResult = (WSHandlerResult)recv.get(0); 
    // WSSecurityEngineResult wsseResult = (WSSecurityEngineResult)wsResult.getResults().get(0); 
    // String login = wsseResult.getPrincipal().getName(); 
    // System.out.println("login = " + login); 

    depositService.upload(doc.doc, doc.authoritative, doc.owner);

    UploadResponseT resp = new UploadResponseT()
    resp.status="OK"
    resp.addinfo=""
    resp.location=""

    println("Got request ${doc} sending response ${resp}");
    return resp
  }

}
