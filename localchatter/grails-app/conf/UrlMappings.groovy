class UrlMappings {

  static mappings = {

      
    "/$controller/$action?/$id?"{
      constraints {
        // apply constraints here
      }
    }

    "/entry/$id" (controller:"entry", action:"show")
    "/collection/$id" (controller:"collection", action:"show")

    "/admin/data/$collection/$id/$action?"(controller:"sysedit")

    "/"(controller:"home",action:"index")
    "500"(view:'/error')
  }
}
