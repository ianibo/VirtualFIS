package com.k_int.vfis.auth

class VfisAuthority {

	String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
