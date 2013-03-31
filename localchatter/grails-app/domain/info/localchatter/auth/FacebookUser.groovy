package info.localchatter.auth

import info.localchatter.auth.User

class FacebookUser {

	long uid
  String accessToken
  Date accessTokenExpires

	static belongsTo = [user: User]

	static constraints = {
		uid unique: true
	}
}
