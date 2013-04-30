package com.donniewest.titan
import com.github.kevinsawicki.http.HttpRequest
import net.liftweb.json._
import com.donniewest.titan.Util.json_extractor


class Discover {

  def find_server(entity: String) = {

    HttpRequest.head(entity).header("Link")

  }


  def find_endpoints(server: String) = {

    implicit val formats = net.liftweb.json.DefaultFormats
    val endpoints_in_json = parse(HttpRequest.get(server).accept("application/vnd.tent.post.v0+json").body())
    Endpoints.setOauth_auth(compact(render(endpoints_in_json \\ "oauth_auth")))
    Endpoints.setEntity(compact(render(endpoints_in_json \\ "entity")))
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
