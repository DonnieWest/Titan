package tent

import com.github.kevinsawicki.http.HttpRequest
import net.liftweb.json._

object Post {

  def send_post(text: String) {

    val json = "{\"type\":\"https://tent.io/types/status/v0#\",\"content\":{\"text\":\"%s\"}}".format(text)
    val contentType = "application/vnd.tent.post.v0+json; type=\"https://tent.io/types/status/v0#\""
    HttpRequest.post(Endpoints.getNew_post).contentType(contentType).authorization(Hawk_Headers.build_headers(json,"POST",Endpoints.getNew_post, false, "application/vnd.tent.post.v0+json")).send(json).body

  }

  def retrieve_feed() {

    //TODO: make method capable of handling various post numbers, maybe even post types?
    val json_post_feed = parse(HttpRequest.get(Endpoints.getPost_feed + "?types=https%3A%2F%2Ftent.io%2Ftypes%status%2Fv0%23").accept("application/vnd.tent.posts-feed.v0+json").authorization(Hawk_Headers.build_headers("","GET", Endpoints.getPost_feed + "?types=https%3A%2F%2Ftent.io%2Ftypes%status%2Fv0%23", false , "application/vnd.tent.post.v0+json")).body)
     //bizarrely, this returns not only Status Posts but also Credential posts? Check into if this is a Tent side bug or me
    case class App(id: String, name: String, url: String) {

      def getInfo = Map("id" -> id, "name" -> name, "url" -> url)

    }


    case class Content(text: String) {

      def getText = text

    }

    case class Post(app: App , content: Content, entity: String, id: String, published_at: String, `type`: String){

      def getInfo = Map("App" -> app.getInfo, "content" -> content.getText, "entity" -> entity, "id" -> id, "published" -> published_at, "type" -> `type`)
    }

    case class Status_Posts(posts: List[Post]){

      def getPosts = posts

    }


    implicit val formats = DefaultFormats

    json_post_feed.extract[Status_Posts].getPosts

  }


}