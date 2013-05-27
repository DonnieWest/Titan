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

    /*
Case classes implemented:

"entity"
"post_id"
"app_name"    X
"post_type"
"mentions"    X
"published"
"content"     X
"location"
"media"
     */
    case class App(name: String, url: String) {

      def getInfo = List(name, url)

    }

    case class Mentions(mentions: List[String]) {

      def getMentions = mentions

    }

    object Mentions {

      def apply: Mentions = Mentions(List(""))

    }

    case class Content(text: String) {

      def getText = text

    }

    case class ID(id: String) {

      def getId = id //TODO: Test. Make sure it doesn't extract both the Post ID and the Post version ID

    }

    case class Published(published_at: BigInt) {

      def getTime = published_at

    }

    case class Type(`type`: String) {

      def getType = `type`

    }

    case class Status_Post(app: App , content: Content, mentions: Mentions){

      def getInfo = List(app.getInfo(0),app.getInfo(1), content.getText, mentions.getMentions)    //TODO: Convert to Hashmap, include the rest of this craziness

    }

    case class Status_Posts(data: List[Status_Post]) {

      def getData = data.map(i => List(i.getInfo(0), i.getInfo(1), i.getInfo(2)))    //TODO: Make this a list of HashMaps

    }

    implicit val formats = DefaultFormats

    json_post_feed.extract[Status_Posts]

/*    val mDbHelper = new Feed_database(getContext)
    val db = mDbHelper.getWritableDatabase
    val values = new ContentValues*/

  }


}
