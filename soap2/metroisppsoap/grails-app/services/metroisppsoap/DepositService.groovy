package metroisppsoap

import uk.gov.dcsf.ispp.webservice.*;
import uk.gov.dcsf.ispp.webservice.encodedtypes.*;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.xml.ws.WebServiceContext;
 

@WebService(serviceName = "ISPPDepositImplService",
            portName="ISPPPortTypePort",
            endpointInterface = "uk.gov.dcsf.ispp.webservice.ISPPPortType",
            targetNamespace = "http://dcsf.gov.uk/ISPP/Webservice") // wsdlLocation = "http://localhost:8080/metroisppsoap/deposit?wsdl")
class DepositService implements ISPPPortType {


  @Resource
  private WebServiceContext context;
 
  @WebMethod
  public UploadResponseT upload( UploadRequestT doc) {
    UploadResponseT resp = new UploadResponseT()
    return resp;
  }
}
