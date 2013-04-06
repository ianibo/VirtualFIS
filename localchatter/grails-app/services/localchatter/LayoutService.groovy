package localchatter

class LayoutService {

  def mongoTypeService

  /**
   * Generate a layout based on the object information
   */
  def generateDefaultLayout(object_type_information, object_instance) {
    def result = [:]
    try {
      result.name="Generated Layout"
      result.fields = []
      object_type_information.properties.each { p ->
        switch ( p.type ) {
          case 'java.lang.String':
            result.fields.add([name:p.name,label:p.name,type:'string'])
            break;
          case 'java.lang.Integer':
            result.fields.add([name:p.name,label:p.name,type:'integer'])
            break;
          case 'java.lang.Long':
            result.fields.add([name:p.name,label:p.name,type:'integer'])
            break;
          case 'com.mongodb.BasicDBObject':
            // Recurse into the attached object value
            log.debug("Process nested object ${p.name}");
            def sub_object_type_info = mongoTypeService.extractTypeInfoFor(object_instance[p.name])
            def sub_object_layout = generateDefaultLayout(sub_object_type_info[0],object_instance[p.name])
            result.fields.add([name:p.name,label:p.name, type:'object', layout:sub_object_layout])
            break;
          default:
            log.debug("Skipping ${p.name} with type ${p.type}");
            break;
        }
      }
    }
    catch ( Exception e ) {
      log.error("Problem generating default layout for object ${object_instance}, extracted type info: ${object_type_information}",e);
    }

    return result
  }
}
