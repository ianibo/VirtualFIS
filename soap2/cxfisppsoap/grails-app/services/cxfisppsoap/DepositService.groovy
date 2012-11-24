package cxfisppsoap

import java.security.MessageDigest

class DepositService {

  def upload(file,authoritative,owner,user) {

    def result = [:]

    log.debug("upload: file len=${file.length()}");
    log.debug("        ${authoritative}");
    log.debug("        ${owner}");
    log.debug("        ${user}");

    try {
      def xs=new net.sf.json.xml.XMLSerializer();
      xs.setSkipNamespaces( false );
      xs.setTrimSpaces( false );
      xs.setRemoveNamespacePrefixFromElements( true );

      result.json = xs.read(file)

      result.json.cksum = chksum(file);

      if ( result['@schemaLocation']?.startsWith('http://dcsf.gov.uk/XMLSchema/Childcare') ) {
        log.debug("ECD Record");
        result.json.'$schema' = "http://purl.org/jsonschema/ecd";
      }
      else {
        if ( result['@schemaLocation']?.startsWith('http://dcsf.gov.uk/XMLSchema/ServiceDirectory') ) {
          log.debug("FSD Record");
          result.json.'$schema' = "http://purl.org/jsonschema/fsd";
        }
        else {
          log.debug("Unknown type");
        }
      }

      log.debug("Converted ${result.json}");

    }
    catch ( Exception e ) {
      log.error("error",e);
    }
    finally {
      log.debug("upload service completed");
    }
  }

  def chksum(file) {
    MessageDigest md5_digest = MessageDigest.getInstance("MD5");
    InputStream md5_is = new java.io.ByteArrayInputStream(file.getBytes());
    byte[] md5_buffer = new byte[8192];
    int md5_read = 0;
    while( (md5_read = md5_is.read(md5_buffer)) >= 0) {
      md5_digest.update(md5_buffer, 0, md5_read);
    }
    md5_is.close();
    byte[] md5sum = md5_digest.digest();
    String md5sumHex = new BigInteger(1, md5sum).toString(16);

    md5sumHex
  }
}
