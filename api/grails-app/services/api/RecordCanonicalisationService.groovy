package api

class RecordCanonicalisationService {

  def gazetteerService

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
      def geocode = gazetteerService.geocode(result.postcode)
      if ( geocode ) {
        // log.debug("geocode result: ${geocode}");
        result.position = [lat:geocode.geometry?.location?.lat, lng:geocode.geometry?.location?.lng]
        result.vpne = [lat:geocode.geometry.viewport?.northeast.lat, lng:geocode.geometry.viewport?.northeast.lng]
        result.vpsw = [lat:geocode.geometry.viewport?.southwest.lat, lng:geocode.geometry.viewport?.southwest.lng]
        result.posttown = extractGeoFeature(geocode,'postal_town');
        result.outcode = result.postcode.substring(0,result.postcode.indexOf(' '));
        result.locality = extractGeoFeature(geocode,'locality');
        result.county = extractGeoFeature(geocode,'administrative_area_level_2');
        result.localauthority = extractGeoFeature(geocode,'political');
      }
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
