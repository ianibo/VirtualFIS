package com.k_int.vfis

import com.k_int.vfis.*;
import groovy.util.slurpersupport.GPathResult
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.*
import java.nio.charset.Charset
import grails.converters.*
import java.security.MessageDigest

class DataSyncService {

  def sync() {
    // Sync orgs with the public list from http://www.openfamilyservices.org.uk/ofs/data/orgs.json
    def data_url = new HTTPBuilder( 'http://www.openfamilyservices.org.uk/ofs/data/orgs.json' )
    // data_url.auth.basic feed_definition.target.identity, feed_definition.target.credentials

    data_url.request(GET) {request ->

      response.success = { resp, data ->
        log.debug("response status: ${resp.statusLine}")
        log.debug("Response data code: ${data}");
        data.each { r ->
          if ( r.identifier != null ) {
            Organisation o = Organisation.findByIdentifier(r.identifier)
            if ( o != null ) {
              o = new Organisation(
                                   identifier:r.identifier, 
                                   code:r.shortCode, 
                                   email:r.email, 
                                   contactEmail:r.contactEmail, 
                                   name:r.name, 
                                   postcode:r.postcode, 
                                   description:r.description, 
                                   disclaimer:r.sourceDisclaimer, 
                                   iconURL:r.iconURL).save(flush:true);
              // {"office":null,"thoroughfare":null,"locality":null,"dependentLocality":null,"region":null,"contactTelephone":null,"contactFax":null,"showLogo":null,"subType":null}

            }
          }
        }
  
        // feed_definition.jsonResponse = data as JSON
        // feed_definition.status=3
        // feed_definition.statusMessage="Deposit:OK - code:${data?.code} / status:${data.status} / message:${data.message}";
        // if ( ( data.resource_identifier != null ) && ( data.resource_identifier.length() > 0 ) )
        // feed_definition.resourceIdentifier=data.resource_identifier
        // feed_definition.checksum = md5sumHex
            // assert resp.statusLine.statusCode == 200
      }

      response.failure = { resp ->
        log.error("Failure - ${resp}");
      }
    }

  }
}
