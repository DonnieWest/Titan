package tent

import com.github.kevinsawicki.http.HttpRequest
import net.liftweb.json._
import scala.collection.mutable

object Post {

  def send_post(text: String) {

    val json = "{\"type\":\"https://tent.io/types/status/v0#\",\"content\":{\"text\":\"%s\"}}".format(text)
    val contentType = "application/vnd.tent.post.v0+json; type=\"https://tent.io/types/status/v0#\""
    HttpRequest.post(Endpoints.getNew_post).contentType(contentType).authorization(Hawk_Headers.build_headers_after_authentication(json,"POST",Endpoints.getNew_post, "application/vnd.tent.post.v0+json")).send(json).body

  }

  def retrieve_feed() {

    //TODO: make method capable of handling various post numbers, maybe even post types?
    val json_post_feed = parse(HttpRequest.get(Endpoints.getPost_feed).accept("application/vnd.tent.posts-feed.v0+json").authorization(Hawk_Headers.build_headers("","GET",Endpoints.getPost_feed, "application/vnd.tent.post.v0+json")).body)

    case class App(name: String, url: String) {

      def getInfo = List(name, url)

    }

    case class Mentions(mentions: List[String]) {

      def getMentions = mentions

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

      def getInfo = List(app.getInfo(0), app.getInfo(1), content.getText, mentions.getMentions)    //TODO: Convert to Hashmap, include the rest of this craziness

    }

    case class Status_Posts(data: List[Status_Post]) {

      def getData = data.map(i => mutable.HashMap("name" -> i.getInfo(0), "url" -> i.getInfo(1), "content" -> i.getInfo(2), "mentions" -> i.getInfo(3)))    //TODO: Make this a list of HashMaps

    }

    implicit val formats = DefaultFormats

    json_post_feed.extract[Status_Posts]

  }


}
