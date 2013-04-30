package com.donniewest.titan
import com.github.kevinsawicki.http.HttpRequest
import net.liftweb.json._
import android.util.Log


class Discover {

  def find_server(entity: String) = {

    val Link = HttpRequest.head(entity).header("Link")
    entity + Link.split("<")(1).split(">")(0) //extract the necessary server endpoint and return
  }


  def find_endpoints(server: String) = {

    implicit val formats = net.liftweb.json.DefaultFormats
    val endpoints_in_json = parse(HttpRequest.get(server).accept("application/vnd.tent.post.v0+json").body())
    Log.e("Endpoints", "The endpoints are " + compact(render(endpoints_in_json)))
    Endpoints.setOauth_auth(compact(render(endpoints_in_json \\ "oauth_auth")))
    Endpoints.setEntity(compact(render(endpoints_in_json \\ "entity")).split("\"")(3)) //returns two different entity locations, trimming it down!
    Endpoints.setOauth_token(compact(render(endpoints_in_json \\ "oauth_token")))
    Endpoints.setPost(compact(render(endpoints_in_json \\ "post")))
    Endpoints.setPost_feed(compact(render(endpoints_in_json \\ "posts_feed")))
    Endpoints.setNew_post(compact(render(endpoints_in_json \\ "new_post")))
    Endpoints.setAttachment(compact(render(endpoints_in_json \\ "attachment")))
    Endpoints.setPost_attachment(compact(render(endpoints_in_json \\ "post_attachment")))
    Endpoints.setBatch(compact(render(endpoints_in_json \\ "batch")))
    Endpoints.setServer_info(compact(render(endpoints_in_json \\ "server_info")))


  }

}
