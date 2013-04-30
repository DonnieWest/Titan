package com.donniewest.titan

import com.github.kevinsawicki.http.HttpRequest


object Registration {

  def registration() = {

     val post_location_header = HttpRequest.post(Endpoints.getNew_post).contentType("application/vnd.tent.post.v0+json; type=\"https://tent.io/types/app/v0#\"").send(Json.registration).header("Link")

     val post_location = Endpoints.getEntity + post_location_header.split("<")(1).split(">")(0)

     val json_hawk_creds = HttpRequest.get(post_location).accept("application/vnd.tent.post.v0+json").body

     //to do - extract hawk_creds from json, put in a credentials object for access! Needed are "hawk_key" and "hawk_algorithm" and "post" for the client-id

  }




}
