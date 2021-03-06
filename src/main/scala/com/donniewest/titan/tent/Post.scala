package com.donniewest.titan.tent

import com.github.kevinsawicki.http.HttpRequest
import net.liftweb.json._


object Post {

  def send_post(text: String) {

//    takes in text and sends a simple status post

    val json = "{\"type\":\"https://tent.io/types/status/v0#\",\"content\":{\"text\":\"%s\"}}".format(text)
    val contentType = "application/vnd.tent.post.v0+json; type=\"https://tent.io/types/status/v0#\""
    HttpRequest.post(Endpoints.getNewPost).contentType(contentType).authorization(Hawk_Headers.build_headers(json,"POST",Endpoints.newPost, false, "application/vnd.tent.post.v0+json", context.getApplicationContext)).send(json).body

  }

  def retrieve_feed = {

//    retrieves the tent feed and stores in database, unfortunately requires context to work with Sugar ORM

    //TODO: make method capable of handling various post numbers, maybe even post types?
    val json_post_feed = parse(HttpRequest.get(Endpoints.postFeed + "?types=https%3A%2F%2Ftent.io%2Ftypes%status%2Fv0%23").accept("application/vnd.tent.posts-feed.v0+json").authorization(Hawk_Headers.build_headers("","GET", Endpoints.postFeed + "?types=https%3A%2F%2Ftent.io%2Ftypes%status%2Fv0%23", false , "application/vnd.tent.post.v0+json")).body)
     //bizarrely, this returns not only Status Posts but also Credential posts? Check into if this is a Tent side bug or me
    case class App(id: String, name: String, url: String) {

      def getID = id
      def getName = name
      def getURL = url

     }


    case class Content(text: String) {

      def getText = text

    }

    case class Post(app: App , content: Content, entity: String, id: String, published_at: String, `type`: String){

      def getInfo = Map(
        "app_name" -> app.getName,
        "app_url" -> app.getURL,
        "app_id" -> app.getID,
        "content" -> content.getText,
        "entity" -> entity,
        "id" -> id,
        "published" -> published_at,
        "type" -> `type`)

      //maybe convert this into a series of methods that retrieve this information? Starting to think Maps are inefficient
    }

    case class Status_Posts(posts: List[Post]){

      def getPosts = posts

    }


    implicit val formats = DefaultFormats

    val post_feed = json_post_feed.extract[Status_Posts].getPosts
    true

  }


}
