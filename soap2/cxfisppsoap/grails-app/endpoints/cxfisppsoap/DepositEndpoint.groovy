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

    static expose = EndpointType.JAX_WS_WSDL
    static wsdl = 'resources/deposit.wsdl'

    public UploadResponseT upload( UploadRequestT doc) {
      UploadResponseT resp = new UploadResponseT()
      resp.status="OK"
      resp.addinfo=""
      resp.location=""
      println("Got request ${doc} sending response ${resp}");
      return resp
    }

}
