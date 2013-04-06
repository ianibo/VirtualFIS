package localchatter

class MongoTypeService {

  def extractTypeInfoFor(mongo_object) {

    def result = []

    log.debug("Request to extract type info for ${mongo_object._id}");
    if ( mongo_object.__types ) {
    }
    else {
      result.add(reflectBasicTypeInfoFromObject(mongo_object));
    }

    result
  }

  def reflectBasicTypeInfoFromObject(mongo_object) {
    log.debug("No type information present. Generating reflected stub");
    def result = [:]
    result.name="__object"
    def properties = []
    mongo_object.keySet().each { k ->
      log.debug("Adding property ${k}");
      properties.add([name:k,type:k.class.name])
    }
    result.properties = properties
    result;
  }
}
