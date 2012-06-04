class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(controller:'frontpage',action:'index')
		"500"(view:'/error')

                "/org/$shortcode/content/$id/edit"(controller:'content',action:'edit')
	}
}
