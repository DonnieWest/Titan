package com.donniewest.titan.Authentication

import com.github.kevinsawicki.http.HttpRequest
import net.liftweb.json._

object Discover {

  def find_server(entity: String) = {

    val Link = HttpRequest.head(entity).header("Link")
    entity + Link.split("<")(1).split(">")(0) //extract the necessary server endpoint, return it
  }


  def extract_endpoints(server: String) {

    //takes in the server from find_server, extracts the endpoints for me to post and authenticate + etc

    val endpoints_in_json = parse(HttpRequest.get(server).accept("application/vnd.tent.post.v0+json").body())
    Endpoints.setOauth_auth(compact(render(endpoints_in_json \\ "oauth_auth")).replace("\"",""))
    Endpoints.setEntity(compact(render(endpoints_in_json \\ "entity")).split("\"")(3).replace("\"","")) //returns two different entity locations, trimming it down!
    Endpoints.setOauth_token(compact(render(endpoints_in_json \\ "oauth_token")).replace("\"",""))
    Endpoints.setPost(compact(render(endpoints_in_json \\ "post")).replace("\"",""))
    Endpoints.setPost_feed(compact(render(endpoints_in_json \\ "posts_feed")).replace("\"",""))
    Endpoints.setNew_post(compact(render(endpoints_in_json \\ "new_post")).replace("\"",""))
    Endpoints.setAttachment(compact(render(endpoints_in_json \\ "attachment")).replace("\"",""))
    Endpoints.setPost_attachment(compact(render(endpoints_in_json \\ "post_attachment")).replace("\"",""))
    Endpoints.setBatch(compact(render(endpoints_in_json \\ "batch")).replace("\"",""))
    Endpoints.setServer_info(compact(render(endpoints_in_json \\ "server_info")).replace("\"",""))


  }

}
