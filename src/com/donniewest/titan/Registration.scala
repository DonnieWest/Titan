package com.donniewest.titan

import com.github.kevinsawicki.http.HttpRequest
import net.liftweb.json._


object Registration {

  def register() = {

     val post_location_header = HttpRequest.post(Endpoints.getNew_post).contentType("application/vnd.tent.post.v0+json; type=\"https://tent.io/types/app/v0#\"").send(Json.registration).header("Link")

     val post_location = Endpoints.getEntity + post_location_header.split("<")(1).split(">")(0)

     val json_hawk_creds = parse(HttpRequest.get(post_location).accept("application/vnd.tent.post.v0+json").body)

     Credentials.setHawk_algorithm(compact(render(json_hawk_creds \\ "hawk_algorithm")))
     Credentials.setHawk_key(compact(render(json_hawk_creds \\ "hawk_key")))
     Credentials.setClient_id(compact(render(json_hawk_creds \\ "post")))
     Credentials.setHawk_id(compact(render(json_hawk_creds \\ "id")))

  }


  def Oauth() = {

    val code = httpRequest.get(Endpoints.getOauth_auth + "?clientid=" + Credentials.getClient_id + "&state=" + Creds.state).header("Location")



  }

}
