package api

import static groovyx.net.http.Method.*
import groovyx.net.http.RESTClient
import groovyx.net.http.*

class ShortcodeService {

  def mongoService

  def getShortcodeFor(type,value,description) {

    // If we locate an exact match, we return it
    // Otherwise, we generate shortcodes until we get one that matches
    def mdb = mongoService.getMongo().getDB('localchatter')
    def shortcode_record = mdb.shortcodes.findOne(type:type,value:value)
    if ( shortcode_record == null ) {
      shortcode_record = createShortcodeRecordFor(type,value,description,mdb)
    }
    shortcode_record
  }

  def createShortcodeRecordFor(type,value,description,mdb) {
    def new_shortcode=description.trim().toLowerCase().replaceAll("[^\w\s]","")
    int i=2;
    while(mdb.shortcodes.findOne(shortcode:new_shortcode)) {
      new_shortcode = "${new_shortcode}_${i++}"
      if ( i > 100 ) {
        throw new Exception("Too many attempts to find a unique shortcode");
      }
    }

    log.debug("Settled on ${new_shortcode}");

    def new_shortcode_record = [
      shortcode:new_shortcode,
      type:type,
      value:value,
      description:description
    ]

    mdb.shortcodes.save(new_shortcode_record);

    log.debug("created a new shortcode: ${new_shortcode_record}");
    new_shortcode_record
  }

}
