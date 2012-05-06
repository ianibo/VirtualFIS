package com.k_int.vfis

class PersonOrg {

    Long dateRequested
    Long dateActioned
    int status  // 0=Pending, 1=Approved, 2=Rejected
    String role
    com.k_int.vfis.auth.VfisPerson actionedBy

    static belongsTo = [org: com.k_int.vfis.Organisation, 
                        person:com.k_int.vfis.auth.VfisPerson]
  
    static constraints = {
      dateActioned(nullable:true)
      actionedBy(nullable:true)
    }
}
