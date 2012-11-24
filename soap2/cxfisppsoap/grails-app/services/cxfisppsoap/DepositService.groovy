package cxfisppsoap

class DepositService {

  def upload(file,authoritative,owner,user) {
    log.debug("upload: file len=${file.length()}");
    log.debug("        ${authoritative}");
    log.debug("        ${owner}");
    log.debug("        ${user}");

    try {
      def xs=new net.sf.json.xml.XMLSerializer();
      xs.setSkipNamespaces( true );
      xs.setTrimSpaces( true );
      xs.setRemoveNamespacePrefixFromElements( true );
      def result = xs.read(file)
      result.'$schema' = "http://purl.org/jsonschema/ecd";
      log.debug("Converted ${result}"); // Result of conversion: ${result}")
    }
    catch ( Exception e ) {
      log.error("error",e);
    }
    finally {
      log.debug("upload service completed");
    }
  }
}
