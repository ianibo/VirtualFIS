package localchatter

class LayoutService {

  /**
   * Generate a layout based on the object information
   */
  def generateDefaultLayout(object_type_information) {
    def result = [:]
    result.name="Generated Layout"
    result.fields = []
    object_type_information.properties.each { p ->
      switch ( p.type ) {
        case 'java.lang.String':
          result.fields.add([label:p.name,type:'string'])
          break;
        default:
          break;
      }

    }

    return result
  }
}
