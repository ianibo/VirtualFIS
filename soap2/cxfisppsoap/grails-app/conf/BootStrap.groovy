// import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor
// import org.apache.ws.security.handler.WSHandlerConstants


import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor
import org.apache.ws.security.WSConstants
import org.apache.ws.security.WSSecurityEngine
import org.apache.ws.security.WSSecurityException
import org.apache.ws.security.handler.WSHandlerConstants
import org.apache.ws.security.validate.UsernameTokenValidator
import org.apache.ws.security.validate.Validator

import javax.xml.namespace.QName

// http://www.christianoestreich.com/2012/04/grails-cxf-interceptor-injection/

class BootStrap {

  def depositEndpointFactory

  def init = { servletContext ->

    Map<String, Object> inProps = [:]
    inProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
    inProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
    Map<QName, Validator> validatorMap = new HashMap<QName, Validator>();
    validatorMap.put(WSSecurityEngine.USERNAME_TOKEN, new UsernameTokenValidator() {

      @Override
      protected void verifyPlaintextPassword(org.apache.ws.security.message.token.UsernameToken usernameToken, 
                                             org.apache.ws.security.handler.RequestData data) throws org.apache.ws.security.WSSecurityException {
        println("verifyPlaintextPassword");
        if(data.username == "wsuser" && usernameToken.password != "secret") {
          throw new WSSecurityException("password mismatch")
        } else {
          println "user name and password were correct!"
        }
      }
    });
    inProps.put(WSS4JInInterceptor.VALIDATOR_MAP, validatorMap);
    if ( depositEndpointFactory ) {
      // depositEndpointFactory.getInInterceptors().add(new WSS4JInInterceptor(inProps))
    }
    else {
      log.error("NO SECURE SERVICE FACTORY");
    }
  }

  def destroy = {
  }
}
