#!/usr/bin/groovy

@Grab(group='net.sf.json-lib', module='json-lib', version='2.4', classifier='jdk15')
@Grab(group='com.github.groovy-wslite', module='groovy-wslite', version='0.7.1')

import net.sf.json.JSONObject
import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion
import wslite.http.auth.*

// def json = "{name=\"json\",bool:true,int:1,double:2.2,func:function(a){ return a; },array:[1,2]}";
// def jsonObject = JSONObject.fromObject( json );
// print jsonObject

testUpload(args[0])


def testUpload(filename) {
  SOAPClient client = new SOAPClient("http://localhost:8080/api/soap/deposit")
  client.authorization = new HTTPBasicAuthorization("test", "tset")

  def upload_file = new File(filename)
  def upload_text = upload_file.text

  SOAPResponse response = client.send() {
    envelopeAttributes "xmlns:ispp": "http://dcsf.gov.uk/ISPP/Webservice/encodedTypes"
    body {
      'ispp:uploadRequest' {
        doc(upload_text)
        authoritative(true)
        owner('ianibbo')
      }
    }
  }

  def methodResponse = response.body
  println("Call completed ${methodResponse}");

}


