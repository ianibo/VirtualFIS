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
    result.ofstedUrn = record.orig?.ProviderDescription?.ProviderDetails?.OfstedURN
    result.childcareType = record.orig?.ProviderDescription?.ProviderDetails?.ChildcareType

    if ( result.postcode ) {
      def geocode = gazetteerService.geocode(result.postcode)
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
}
