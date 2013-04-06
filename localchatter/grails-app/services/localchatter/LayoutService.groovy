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
          case 'com.mongodb.BasicDBList':
            // Want to be able to support several variant list layouts: Tabular, where rows are reasonably consistent,
            // Object, were rows are heterogenious, and some variant for *big* lists. For now, tabular
            //
            //If there is at least 1 item in the list, use the 0th element as a template
            def tab_layout = [label:p.name, name:p.name, type:'tablist']
            log.debug("Handle list property (${p.name})");
            if ( object_instance[p.name].size() > 0 ) {
              // Is the row a scalar, or an object?
              if ( object_instance[p.name][0].getClass().name == 'com.mongodb.BasicDBObject' ) {
                def first_row_type_info = mongoTypeService.extractTypeInfoFor(object_instance[p.name][0])
                log.debug("First row of table: ${object_instance[p.name][0]}, generated type info: ${first_row_type_info}");
                tab_layout.columns = []
                first_row_type_info[0].properties.each { row_prop ->
                  log.debug("Adding column: ${row_prop}");
                  tab_layout.columns.add([name:row_prop.name,label:row_prop.name]);
                }
                result.fields.add(tab_layout);
              }
              else {
                log.debug("Not a list of objects, not handled yet...");
              }
            }
            tab_layout
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
