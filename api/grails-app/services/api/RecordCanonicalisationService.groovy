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
    result.title = record.orig?.ProviderDescription?.DC_Title
    result.description = record.orig?.ProviderDescription?.Description?.DC_DESCRIPTION?.'#text'
    result.postcode = record.orig?.ProviderDescription?.ProviderDetails?.SettingDetails?.PostalAddress?.A_5LineAddress?.PostCode
    result.addr = []
    record.orig?.ProviderDescription?.ProviderDetails?.SettingDetails?.PostalAddress?.A_5LineAddress.'Line'.each { l ->
      result.addr.add(l)
    }

    result.ofstedUrn = record.orig?.ProviderDescription?.ProviderDetails?.OfstedURN
    result.childcareType = record.orig?.ProviderDescription?.ProviderDetails?.ChildcareType

    if ( result.postcode ) {
      def geocode = newGazetteerService.geocode(result.postcode)
      if ( geocode ) {
        // log.debug("geocode result: ${geocode}");
        result.position = [lat:geocode.geometry?.location?.lat, lng:geocode.geometry?.location?.lng]
        result.outcode = result.postcode.substring(0,result.postcode.indexOf(' '));

        if ( result.outcode && ( result.outcode.length() > 0 ) ) {
          shortcodeService.getShortcodeFor('outcode',result.outcode,result.outcode)
        }

        if ( geocode.administrative.district ) {
          result.district = geocode.administrative.district.title
          result.district_facet = "${geocode.administrative.district.snac}:${geocode.administrative.district.title}"
          shortcodeService.getShortcodeFor('district',geocode.administrative.district.snac,geocode.administrative.district.title)
        }
        if ( geocode.administrative?.ward ) {
          result.ward = geocode.administrative.ward.title
          result.ward_facet = "${geocode.administrative.ward.snac}:${geocode.administrative.ward.title}"
          shortcodeService.getShortcodeFor('ward',geocode.administrative.ward.snac,geocode.administrative.ward.title)
        }
        if ( geocode.administrative?.county ) {
          result.ward = geocode.administrative.county.title
          result.ward_facet = "${geocode.administrative.county.snac}:${geocode.administrative.county.title}"
          shortcodeService.getShortcodeFor('county',geocode.administrative.county.snac,geocode.administrative.county.title)
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
