package localchatter

import info.localchatter.auth.*;
import com.the6hours.grails.springsecurity.facebook.FacebookAuthToken
import org.springframework.social.facebook.api.impl.FacebookTemplate
import org.springframework.social.facebook.api.Facebook
import org.springframework.social.facebook.api.FacebookProfile

class FacebookAuthService {

  void onCreate(FacebookUser user, FacebookAuthToken token) {
    log.debug("FacebookAuthService::onCreate");

    Facebook facebook = new FacebookTemplate(token.accessToken.accessToken)
    FacebookProfile fbProfile = facebook.userOperations().userProfile

    log.debug("Got facebook");

    //fill the name
    //fieldname ('fullname' at this example) is up to you
    user.user.displayName = fbProfile.name
    user.user.email = fbProfile.email

    // user.user needs to be saved....
   
    log.debug("Setting displayName to ${fbProfile.name}");
    log.debug("Setting email to ${fbProfile.email}");
  }
}
