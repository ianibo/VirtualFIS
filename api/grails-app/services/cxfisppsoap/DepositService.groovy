package cxfisppsoap

import java.security.MessageDigest
import javax.annotation.PostConstruct;
import net.sf.json.JSON

// http://www.xml.com/pub/a/2006/05/31/converting-between-xml-and-json.html

class DepositService {

  def mongoService
  def recordCanonicalisationService
  def elasticSearchService

  @PostConstruct
  def init() {
    log.debug("Registering json null encoder")
    def jsonnull_encoder = new org.bson.Transformer() {
      Object transform(Object o) {
        return null
      }
    };

    // Attempt to rewrite input field names, substituting . for _
    def jsonobject_encoder = new org.bson.Transformer() {
      // Transform the json object into a simple hashmap, where the keys have . replaced with _
      Object transform(Object o) {
        def result;
        // def result = new org.bson.BasicBSONObject()
        if ( o instanceof net.sf.json.JSONObject ) {
          result = new java.util.HashMap()
          net.sf.json.JSONObject jo = (net.sf.json.JSONObject) o
          jo.keys().each { key ->
            result.put(key.replace('.','_'), org.bson.BSON.applyEncodingHooks(jo.get(key)))
          }
        }
        else {
          result = o
        }

        return result
      }
    };

    org.bson.BSON.addEncodingHook(net.sf.json.JSONNull,jsonnull_encoder)
    org.bson.BSON.addEncodingHook(net.sf.json.JSONObject,jsonobject_encoder)
  }

  def upload(file,authoritative,owner,user) {

    def result = [:]

    log.debug("upload: file len=${file.length()}, ${authoritative}, ${owner}, ${user}");

    try {
      def xs=new net.sf.json.xml.XMLSerializer();
      xs.setSkipNamespaces( true );
      xs.setTrimSpaces( true );
      // xs.setSkipWhitespace( true ); - This causes many data elements to end up null
      xs.setForceTopLevelObject( true );
      xs.setRemoveNamespacePrefixFromElements( true );

      file = file.replaceAll('<DC\\.Date\\.','<DC_Date_');
      file = file.replaceAll('</DC\\.Date\\.','</DC_Date_');
      file = file.replaceAll('<DC\\.','<DC_');
      file = file.replaceAll('</DC\\.','</DC_');
      file = file.replaceAll('<DCTerms\\.','<DCTerms_');
      file = file.replaceAll('</DCTerms\\.','</DCTerms_');

      JSON j = xs.read(file)
      result.orig = j

      log.debug(j.toString());
      result.owner = owner;
      result.cksum = chksum(file);
      result.timestamp = System.currentTimeMillis();
      
      if ( result.orig.ProviderDescription ) {
        // log.debug("ECD Record");
        result.'__schema' = "http://purl.org/jsonschema/ecd";
        result.docid = "${owner}:${j.ProviderDescription.'DC_Identifier'}"
      }
      else {
        if ( result.orig.ServiceDescription ) {
          log.debug("FSD Record");
          result.'__schema' = "http://purl.org/jsonschema/fsd";
          result.docid = "${owner}:${j.ServiceDescription.'DC_Identifier'}"
        }
        else {
          log.error("Unknown type");
        }
      }


      // log.debug("looking for docid ${result.docid}");

      def mdb = mongoService.getMongo().getDB('localchatter')
      def reco_record = mdb.sourcerecs.findOne(docid:result.docid)

      if ( reco_record ) {
        // log.debug("Replace existing record");
        mdb.sourcerecs.remove(reco_record)
      }

      // log.debug("Saving...");
      mdb.sourcerecs.save(result);

      // Canonicalise the record
      def canonical_record = recordCanonicalisationService.process(result);

      // Index the canonical record
      if ( canonical_record ) {
        // log.debug("process canonical record");
        org.elasticsearch.groovy.node.GNode esnode = elasticSearchService.getESNode()
        org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

        def future = esclient.index {
          index "localchatter"
          type "resource"
          id result.docid
          source canonical_record
        }
        log.debug("Indexed respidx:${future.response.index}/resptp:${future.response.type}/respid:${future.response.id}")
      }
      
    }
    catch ( Exception e ) {
      log.error("error",e);
    }
    finally {
      // log.debug("upload service completed");
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
