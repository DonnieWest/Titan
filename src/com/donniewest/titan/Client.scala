package com.donniewest.titan

import com.github.kevinsawicki.http.HttpRequest


object Client {

  def authenticate_with_server(entity: String) = {

    val server = Discover.find_server(entity)
    Discover.extract_endpoints(server)
    Registration.register()
    Registration.Oauth()
    true

  }

  def post_send(content: String) = {

    val json = "{\n  \"type\": \"https://tent.io/types/status/v0#\",\n  \"content\": {\n    \"text\": \"%s\"\n  }\n}".format(content)
    HttpRequest.post(Endpoints.getNew_post).contentType("application/vnd.tent.post.v0+json; type=\"https://tent.io/types/status/v0#\"").authorization(Hawk_Headers.build_headers(json,"POST",Endpoints.getNew_post)).send(json)

  }

}
