package com.donniewest.titan

import com.github.kevinsawicki.http.HttpRequest
import com.donniewest.titan.Authentication.{Registration, Hawk_Headers, Endpoints, Discover}
import com.donniewest.titan.Sqlite._
import net.liftweb.json._
import android.content.ContentValues



object Client {

  def authenticate_with_server(entity: String)  {

    val server = Discover.find_server(entity)
    Discover.extract_endpoints(server)
    Registration.register()
    Registration.Oauth()

  }

  def post_send_basic(content: String) = {

    val json = "{\"type\":\"https://tent.io/types/status/v0#\",\"content\":{\"text\":\"%s\"}}".format(content)
    val contentType = "application/vnd.tent.post.v0+json; type=\"https://tent.io/types/status/v0#\""
    HttpRequest.post(Endpoints.getNew_post).contentType(contentType).authorization(Hawk_Headers.build_headers_after_authentication(json,"POST",Endpoints.getNew_post, "application/vnd.tent.post.v0+json")).send(json).body

  }

  def retrieve_your_posts = {
    //TODO: make method capable of handling various post numbers, maybe even post types?
    val json_post_feed = parse(HttpRequest.get(Endpoints.getPost_feed).accept("application/vnd.tent.posts-feed.v0+json").authorization(Hawk_Headers.build_headers_after_authentication("","GET",Endpoints.getPost_feed, "application/vnd.tent.post.v0+json")).body)
    case class App(id: String, name: String, url: String) {

      def getId = id
      def getName = name
      def getUrl = url

    }
    case class Content(text: String) {

      def getText = text

    }


    case class Post(app: App , content: Content){

      def getApp = app
      def getContent = content

    }

    //Right now, running json_post_feed.extract[Post] or List[Post] or etc. is failing. I'm halfway there on the structure, just not quite

    val mDbHelper = new Feed_database(getContext)
    val db = mDbHelper.getWritableDatabase
    val values = new ContentValues()

  }


}
