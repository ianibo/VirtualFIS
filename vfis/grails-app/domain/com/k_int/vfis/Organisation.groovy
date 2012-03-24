package com.k_int.vfis

class Organisation {

  String identifier
  String code
  String email
  String contactEmail
  String name
  String postcode
  String description
  String sourceDisclaimer
  String iconURL

  static hasMany = [adminAssociations: PersonOrg]


  static constraints = {
    email(nullable:true, blank:true)
    contactEmail(nullable:true, blank:true)
    name(nullable:true, blank:true)
    postcode(nullable:true, blank:true)
    description(nullable:true, blank:true)
    sourceDisclaimer(nullable:true, blank:true)
    iconURL(nullable:true, blank:true)
  }
}
