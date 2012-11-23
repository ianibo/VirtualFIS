package cxfisppsoap

import geb.spock.GebReportingSpec
import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import wslite.soap.SOAPVersion

class DepositSpec extends GebReportingSpec {

    SOAPClient client = new SOAPClient("http://localhost:${System.getProperty("server.port", "8080")}/deposit")

    def "upload actually works"() {
        when:
        SOAPResponse response = client.send {
            envelopeAttributes "xmlns:test": "http://test.cxf.grails.org/"
            body {
                'test:upload' {
                    name 'testtest'
                }
            }
        }
        def methodResponse = response.body.getCustomersByNameResponse.return

        then:
        response.httpResponse.statusCode == 200
        response.soapVersion == SOAPVersion.V1_1
        // customerName == methodResponse.name.text()
        // customerType == methodResponse.type.text()

        where:
        1 | 1
        // customerName  | customerType
        // 'Frank'       | CustomerType.PRIVATE.name()
        // 'Super Duper' | CustomerType.BUSINESS.name()
    }
}

