package api

class RecordCanonicalisationService {


  def process(record) {
    log.debug("Process..  schema: ${record.?__schema}");

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
    def result=[:]
    result.docid = record.docid
    // result.title = 
    // result.description = 
    result
  }

  def processFsd(record) {
    def result=[:]
    result.docid = record.docid
    result
  }
}
