package com.donniewest.titan.Authentication

import com.github.kevinsawicki.http.HttpRequest
import net.liftweb.json._
import java.net.{HttpURLConnection, URL}
import android.util.Log
import com.donniewest.titan.Util.json_extractor
import scala.util.Random


object Registration {

  def register() {

     val post_location_header = HttpRequest.post(Endpoints.getNew_post).contentType("application/vnd.tent.post.v0+json; type=\"https://tent.io/types/app/v0#\"").send(Json.registration).header("Link")

     val post_location = post_location_header.split("<")(1).split(">")(0)

     val json_hawk_creds = parse(HttpRequest.get(post_location).accept("application/vnd.tent.post.v0+json").body)

     Credentials.setHawk_algorithm(compact(render(json_hawk_creds \\ "hawk_algorithm")))
     Credentials.setHawk_key(compact(render(json_hawk_creds \\ "hawk_key")).replace("\"",""))
     Credentials.setClient_id(compact(render(json_hawk_creds \\ "post")).replace("\"",""))
     Credentials.setHawk_id(compact(render(json_hawk_creds \\ "id")).split(",")(0).split(":")(1).replace("\"",""))  //I can extract this /nicer/ with the methods in lift-json, but I'm lazy right now
  }


  def Oauth() = {

    val tag = "Oauth"

//    Log.e(tag, "Entering Oauth")

    var state = {
      var random = new Random().nextInt()
      if (random < 0) -random else random
    }

//    val location = HttpRequest.get(Endpoints.getOauth_auth + "?client_id=" + Credentials.getClient_id + "&state=" + state).location  //("Location")
    val locate = new URL(Endpoints.getOauth_auth + "?client_id=" + Credentials.getClient_id + "&state=" + state)
    val located = locate.openConnection().asInstanceOf[HttpURLConnection]
    located.setInstanceFollowRedirects(false)
    val location = located.getHeaderField("Location")  //HAH! On computer, HttpUrlConnection does not follow redirects. Android does. HttpRequest, which is based on HttpUrlconnection, can't turn off redirects

//    Log.e(tag, "yanked code location which is " + location)

    val code = location.split("code=")(1).split("&state=")(0)

//    Log.e(tag, "yanked code which is " + code)

    val json = "{\n  \"code\": \"%s\",\n  \"token_type\": \"https://tent.io/oauth/hawk-token\"\n}".format(code)

//    Log.e(tag, json)

    val json_response = parse(HttpRequest.post(Endpoints.getOauth_token).accept("application/json").authorization(Hawk_Headers.build_headers(json,"POST",Endpoints.getOauth_token)).contentType("application/json").send(json).body)

//    Log.e(tag, "built json_response")

    Temporary_Credentials.setAccess_token(json_extractor.extract(json_response,"access_token"))
    Temporary_Credentials.setHawk_algorithm(json_extractor.extract(json_response,"hawk_algorithm"))
    Temporary_Credentials.setHawk_key(json_extractor.extract(json_response,"hawk_key"))
    Temporary_Credentials.setToken_type(json_extractor.extract(json_response,"token_type"))

//    Log.e(tag, "Finished")


  }

}
