package cxfisppsoap

import java.security.MessageDigest

class DepositService {

  def mongoService

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
      xs.setSkipWhitespace( true );
      xs.setForceTopLevelObject( true );
      xs.setRemoveNamespacePrefixFromElements( true );

      result.orig = xs.read(file)

      log.debug("transformed file: ${result.orig}");

      result.cksum = chksum(file);

      if ( result.orig.ProviderDescription ) {
        log.debug("ECD Record");
        result.'$schema' = "http://purl.org/jsonschema/ecd";
        result.docid = "${owner}:${result.orig.ProviderDescription.'DC.Identifier'}"
      }
      else {
        if ( result.orig.ServiceDescription ) {
          log.debug("FSD Record");
          result.'$schema' = "http://purl.org/jsonschema/fsd";
          result.docid = "${owner}:${result.orig.ServiceDescription.'DC.Identifier'}"
        }
        else {
          log.debug("Unknown type");
        }
      }

      def mdb = mongoService.getMongo().getDB('ispp')
      def reco_record = mdb.sourcerecs.findOne(docid:result.docid)
      if ( reco_record ) {
        log.debug("Replace existing record");
      }
      else {
        log.debug("Saving...");
        mdb.sourcerecs.save(result);
      }

      log.debug("Converted ${result}");
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
