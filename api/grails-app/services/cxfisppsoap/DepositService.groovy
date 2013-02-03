package cxfisppsoap

import java.security.MessageDigest
import javax.annotation.PostConstruct;
import net.sf.json.JSON
import groovy.json.JsonSlurper

// http://www.xml.com/pub/a/2006/05/31/converting-between-xml-and-json.html

class DepositService {

  def mongoService
  def recordCanonicalisationService
  def elasticSearchService
  def newGazetteerService
  def shortcodeService

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
    upload(file,authoritative,owner,user,null);
  }

  def upload(file,authoritative,owner,user, contentType) {
    switch(contentType) {
      case 'text/xml':
        log.debug("Process XML");
        uploadXML(file,authoritative,owner,user, contentType)
        break;
      case 'application/json':
        log.debug("Process JSON");
        uploadJSON(file,authoritative,owner,user, contentType)
        break;
      default:
        log.error("Unhandled submission type: ${contentType}");
        break;
    }
  }

  def uploadJSON(file,authoritative,owner,user, contentType) {
    log.debug("uploadJSON...");
    def json = new JsonSlurper().parseText(file)
    log.debug("Got json ${json}");
    def schema = json.'$schema'
    if ( schema) {
      json.__schema = schema
      json.remove('$schema') // We are storing the record in mongo.. it doesn't like fields starting with $

      log.debug("Schema is present - ${schema} - new rec is ${json}");
      def mdb = mongoService.getMongo().getDB('localchatter')
      def lcidentifier = "${owner}:${json.url}"
      log.debug("resource identifier is ${lcidentifier}");
      def recon_record = mdb.sourcerecs.findOne(provid:lcidentifier)
      if ( recon_record ) {
        updateExistingReconRecord(lcidentifier,mdb,recon_record,json,owner,user)
      }
      else {
        processNewRecordUpload(lcidentifier,mdb,recon_record,json,owner,user)
      }
    }
    else {
      log.debug("No schema in json record - cannot process");
    }
  }

  def updateExistingReconRecord(lcidentifier,mdb,recon_record,json,owner,user) {
    log.debug("updateExistingReconRecord");
  }

  def processNewRecordUpload(lcidentifier,mdb,recon_record,json,owner,user) {
    log.debug("processNewRecordUpload");
    def recon_rec = [:]
    recon_rec.provid = lcidentifier
    recon_rec.orig = json
    recon_rec.owner = owner;
    recon_rec.cksum = chksum(file);
    recon_rec.timestamp = System.currentTimeMillis();
    recon_rec.'__schema' = json.'__schema'
    recon_rec._id = new org.bson.types.ObjectId()

    mdb.sourceRecords.save(recon_rec)

    mdb.currenRecords.save(augment(recon_rec))

    log.debug("Saved reconciliation record");
  }

  def uploadXML(file,authoritative,owner,user, contentType) {

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

      // log.debug(j.toString());
      result.owner = owner;
      result.cksum = chksum(file);
      result.timestamp = System.currentTimeMillis();
      
      if ( result.orig.ProviderDescription ) {
        // log.debug("ECD Record");
        result.'__schema' = "http://purl.org/jsonschema/ecd";
        result.provid = "${owner}:${j.ProviderDescription.'DC_Identifier'}"
      }
      else {
        if ( result.orig.ServiceDescription ) {
          log.debug("FSD Record");
          result.'__schema' = "http://purl.org/jsonschema/fsd";
          result.provid = "${owner}:${j.ServiceDescription.'DC_Identifier'}"
        }
        else {
          log.error("Unknown type");
        }
      }


      // log.debug("looking for docid ${result.docid}");

      def mdb = mongoService.getMongo().getDB('localchatter')
      def reco_record = mdb.sourcerecs.findOne(provid:result.provid)

      if ( reco_record ) {
        // log.debug("Replace existing record");
        mdb.sourcerecs.remove(reco_record)
      }
      else {
        result._id = new org.bson.types.ObjectId()
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
          id result._id.toString()
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


  def augment(source_record) {
    def new_record = source_record

    new_record.shortcode = shortcodeService.getShortcodeFor('resource',source_record._id,source_record.orig.name);

    // 1. Lookin to the address field and see if there is a postcode
    if ( new_record.orig.address.postalCode ) {
      log.debug("Postal code is present");
      def geocode = newGazetteerService.geocode(result.postcode)
      if ( geocode ) {
        log.debug("geocode result: ${geocode}");
        // Needs to be lat,lon for es geo_point type
        new_record.position = [lat:geocode.response.geo.lat, lon:geocode.response.geo.lng]
        new_record.outcode = result.postcode.substring(0,result.postcode.indexOf(' '));
        if ( new_record.outcode )
          new_record.postalArea = (result.outcode =~ /[A-Za-z]*/)[0]


        if ( new_record.outcode && ( result.outcode.length() > 0 ) ) {
          shortcodeService.getShortcodeFor('outcode',new_record.outcode,new_record.outcode)
        }

        if ( geocode.response.administrative ) {
          if ( geocode.response.administrative?.district ) {
            new_record.district = geocode.response.administrative.district.title
            new_record.district_facet = "${geocode.response.administrative.district.snac}:${geocode.response.administrative.district.title}"
            def district_shortcode = shortcodeService.getShortcodeFor('district',geocode.response.administrative.district.snac,geocode.response.administrative.district.title)
            new_record.district_shortcode = district_shortcode.shortcode
          }
          if ( geocode.response.administrative?.ward ) {
            new_record.ward = geocode.response.administrative.ward.title
            if ( new_record.ward ) {
              new_record.ward_facet = "${geocode.response.administrative.ward.snac}:${geocode.response.administrative.ward.title}"
              def ward_shortcode = shortcodeService.getShortcodeFor('ward',geocode.response.administrative.ward.snac,geocode.response.administrative.ward.title)
              new_record.ward_shortcode = ward_shortcode.shortcode
            }
          }
          if ( geocode.response.administrative?.county ) {
            new_record.county = geocode.response.administrative.county.title
            new_record.county_facet = "${geocode.response.administrative.county.snac}:${geocode.response.administrative.county.title}"
            def county_shortcode = shortcodeService.getShortcodeFor('county',geocode.response.administrative.county.snac,geocode.response.administrative.county.title)
            new_record.county_shortcode = county_shortcode.shortcode
          }
        }
        else {
          log.debug("No administrative data in geocode ${geocode}");
        }
      }
    }
    new_record
  }
}
