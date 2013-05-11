package com.donniewest.titan

import com.github.kevinsawicki.http.HttpRequest
import com.donniewest.titan.Authentication.{Registration, Hawk_Headers, Endpoints, Discover}
import android.util.Log


object Client {

  def authenticate_with_server(entity: String) = {
    val tag = "auth"

//    Log.e(tag, "Entering Auth")
    val server = Discover.find_server(entity)
//    Log.e(tag, "finished discovery of server which is " + server)
    Discover.extract_endpoints(server)
//    Log.e(tag, "extracted endpoints")
    Registration.register()
//    Log.e(tag, "registered, getting hawk creds")
    Registration.Oauth()
//    Log.e(tag, "finished!")

  }

  def post_send_basic(content: String) = {

    val json = "{\"type\":\"https://tent.io/types/status/v0#\",\"content\":{\"text\":\"%s\"}}".format(content)
    val contentType = "application/vnd.tent.post.v0+json; type=\"https://tent.io/types/status/v0#\""
    HttpRequest.post(Endpoints.getNew_post).contentType(contentType).authorization(Hawk_Headers.build_headers_after_authentication(json,"POST",Endpoints.getNew_post, "application/vnd.tent.post.v0+json")).send(json).body

  }


}
