#!/usr/bin/groovy

@Grab(group='net.sf.json-lib', module='json-lib', version='2.4', classifier='jdk15')
@Grab(group='com.github.groovy-wslite', module='groovy-wslite', version='0.7.1')

import net.sf.json.JSONObject
import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion

// def json = "{name=\"json\",bool:true,int:1,double:2.2,func:function(a){ return a; },array:[1,2]}";
// def jsonObject = JSONObject.fromObject( json );
// print jsonObject

testUpload()


def testUpload() {
  SOAPClient client = new SOAPClient("http://localhost:8080/cxfisppsoap/deposit")

  client.send(
"""<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:enc="http://dcsf.gov.uk/ISPP/Webservice/encodedTypes">
   <soapenv:Header/>
   <soapenv:Body>
      <enc:uploadRequest>
         <doc>?</doc>
         <!--Optional:-->
         <authoritative>?</authoritative>
         <!--Optional:-->
         <owner>?</owner>
      </enc:uploadRequest>
   </soapenv:Body>
</soapenv:Envelope>""")



  // SOAPResponse response = client.send() {
  //   envelopeAttributes "xmlns:ispp": "http://dcsf.gov.uk/ISPP/Webservice/encodedTypes"
  //   body {
  //     'ispp:uploadRequest' {
  //       doc('this is the doc string')
  //       authoritative(true)
  //       owner('this is the owner')
  //     }
  //   }
  // }

  // def methodResponse = response.body.getCustomersByNameResponse.return
}


