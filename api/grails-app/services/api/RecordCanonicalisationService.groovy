package api

class RecordCanonicalisationService {

  def newGazetteerService
  def shortcodeService

  def process(record) {
    log.debug("Process..  schema: ${record?.__schema}");

    def result = null;

    if ( !record )
      return result;

    if ( !record.__schema )
      return result;


    if ( record.__schema == 'http://purl.org/jsonschema/ecd' ) {
      result = processEcd(record);
    }
    else if ( record.__schema == 'http://purl.org/jsonschema/fsd' ) {
      result = processFsd(record);
    }
    else {
      log.debug("No matching handler");
    }

    result
  }

  def processEcd(record) {

    // Obtain database for geocoding
    // def gazcache_db = mongoService.getMongo().getDB("gazcache")

    def result=[:]
    result.docid = record.docid
    result.provider = record.owner
    result.title = record.orig?.ProviderDescription?.DC_Title
    result.description = record.orig?.ProviderDescription?.Description?.DC_DESCRIPTION?.'#text' + " - (" + record.orig?.ProviderDescription?.ProviderDetails?.ChildcareType + ")"
    result.postcode = record.orig?.ProviderDescription?.ProviderDetails?.SettingDetails?.PostalAddress?.A_5LineAddress?.PostCode
    result.addr = []
    record.orig?.ProviderDescription?.ProviderDetails?.SettingDetails?.PostalAddress?.A_5LineAddress.'Line'.each { l ->
      result.addr.add(l)
    }
    result.certs = []
    result.identifiers = []

    result.ofstedUrn = record.orig?.ProviderDescription?.ProviderDetails?.OfstedURN
    if ( record.orig?.ProviderDescription?.ProviderDetails?.OfstedURN ) {
      result.identifiers.add([namespace:'ofsted',value:record.orig?.ProviderDescription?.ProviderDetails?.OfstedURN])
    }
 
    result.childcareType = record.orig?.ProviderDescription?.ProviderDetails?.ChildcareType

    result.shortcode = shortcodeService.getShortcodeFor('resource',record.docid,result.title).shortcode;
    result.infotypes = [ 'families', 'families/childcare']
    result.privacyLevel = 'PublicListing'

    if ( result.ofstedUrn )
      result.certs.add([cert:'OFSTED', id:result.ofstedUrn, uri:'http://www.ofsted.gov.uk/inspection-reports/find-inspection-report/provider/CARE/'+result.ofstedUrn])

    if ( result.postcode ) {
      def geocode = newGazetteerService.geocode(result.postcode)
      if ( geocode ) {
        log.debug("geocode result: ${geocode}");
        // Needs to be lat,lon for es geo_point type
        result.position = [lat:geocode.response.geo.lat, lon:geocode.response.geo.lng]
        result.outcode = result.postcode.substring(0,result.postcode.indexOf(' '));

        if ( result.outcode && ( result.outcode.length() > 0 ) ) {
          shortcodeService.getShortcodeFor('outcode',result.outcode,result.outcode)
        }

        if ( geocode.response.administrative ) {
          if ( geocode.response.administrative?.district ) {
            result.district = geocode.response.administrative.district.title
            result.district_facet = "${geocode.response.administrative.district.snac}:${geocode.response.administrative.district.title}"
            shortcodeService.getShortcodeFor('district',geocode.response.administrative.district.snac,geocode.response.administrative.district.title)
          }
          if ( geocode.response.administrative?.ward ) {
            result.ward = geocode.response.administrative.ward.title
            result.ward_facet = "${geocode.response.administrative.ward.snac}:${geocode.response.administrative.ward.title}"
            shortcodeService.getShortcodeFor('ward',geocode.response.administrative.ward.snac,geocode.response.administrative.ward.title)
          }
          if ( geocode.response.administrative?.county ) {
            result.ward = geocode.response.administrative.county.title
            result.ward_facet = "${geocode.response.administrative.county.snac}:${geocode.response.administrative.county.title}"
            shortcodeService.getShortcodeFor('county',geocode.response.administrative.county.snac,geocode.response.administrative.county.title)
          }
        }
        else {
          log.debug("No administrative data in geocode ${geocode}");
        }
      }
      else {
        result.geonote ="Unable to resolve ${result.postcode}";
      }
    } 
    else {
      result.geonote ="No postcode present";
    }

    if ( record.orig?.ProviderDescription?.RegistrationDetails ) {
      result.registeredServiceDetails = [:]
      result.registeredServiceDetails.registrationDate = record.orig?.ProviderDescription?.RegistrationDetails.RegistrationDate
    }

    result
  }

  def processFsd(record) {
    def result=[:]
    result.docid = record.docid
    result
  }

  def extractGeoFeature(georecord, featurename) {
    georecord.address_components.find { ac -> ac.types.contains(featurename) }
  }
}
