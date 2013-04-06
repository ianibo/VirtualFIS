package localchatter

class MongoTypeService {

  def extractTypeInfoFor(mongo_object) {

    def result = []

    log.debug("Request to extract type info for ${mongo_object._id} (null indicates an anonymous nested object)");
    if ( mongo_object.__types ) {
    }
    result.add(reflectBasicTypeInfoFromObject(mongo_object));

    result
  }

  def reflectBasicTypeInfoFromObject(mongo_object) {
    log.debug("No type information present. Generating reflected stub - Root is ${mongo_object.getClass()}");
    def result = [:]
    result.name="__object"
    def properties = []
    mongo_object.keySet().each { k ->
      log.debug("Adding property ${k} class ${mongo_object[k]?.getClass()?.name}");
      properties.add([name:k,type:mongo_object[k].getClass().name])
    }
    result.properties = properties
    result;
  }
}
