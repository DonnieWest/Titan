package com.donniewest.titan

import com.github.kevinsawicki.http.HttpRequest
import net.liftweb.json._
import java.net.URL
import android.util.Log


object Registration {

  def register() = {

     val post_location_header = HttpRequest.post(Endpoints.getNew_post).contentType("application/vnd.tent.post.v0+json; type=\"https://tent.io/types/app/v0#\"").send(Json.registration).header("Link")

     val post_location = post_location_header.split("<")(1).split(">")(0)

     val json_hawk_creds = parse(HttpRequest.get(post_location).accept("application/vnd.tent.post.v0+json").body)

     Credentials.setHawk_algorithm(compact(render(json_hawk_creds \\ "hawk_algorithm")))
     Credentials.setHawk_key(compact(render(json_hawk_creds \\ "hawk_key")).replace("\"",""))
     Credentials.setClient_id(compact(render(json_hawk_creds \\ "post")).replace("\"",""))
     Credentials.setHawk_id(compact(render(json_hawk_creds \\ "id")).split(",")(0).split(":")(1).replace("\"",""))
  }


  def Oauth() = {

    val location = HttpRequest.get(Endpoints.getOauth_auth + "?clientid=" + Credentials.getClient_id + "&state=" + Creds.state).header("Location")
    val code_url = location.asInstanceOf[URL]     //ugliness, casting to extract Code from the location, not type safe!
    val code = code_url.getQuery()
    val json = "{\n  \"code\": \"%s\",\n  \"token_type\": \"https://tent.io/oauth/hawk-token\"\n}".format(code)

    val json_response = HttpRequest.post(Endpoints.getOauth_token).accept("application/json").authorization(Hawk_Headers.build_headers(json,"POST",Endpoints.getOauth_token)).contentType("application/json").ok()

    Log.e("Error?", json_response.toString)



  }

}
