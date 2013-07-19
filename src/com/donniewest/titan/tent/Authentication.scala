package tent

import net.liftweb.json._
import com.github.kevinsawicki.http.HttpRequest
import scala.util.Random
import java.net.{HttpURLConnection, URL}
import Util.JsonExtractor

object Authentication {

  def login(entity: String) {

    lazy val state = {
      val random = new Random().nextInt()
      if (random < 0) -random else random
    }
    //Need to find API endpoint
    val serverEndpoint = entity + HttpRequest.head(entity).header("Link").split("<")(1).split(">")(0)

    def extractEndpoint() {

      //takes in the server from find_server, extracts the endpoints for me to post and authenticate + etc

      val endpoints_in_json = parse(HttpRequest.get(serverEndpoint).accept("application/vnd.tent.post.v0+json").body())
      Endpoints.setOauthAuth(JsonExtractor.extract(endpoints_in_json, "oauth_auth"))
      Endpoints.setEntity(compact(render(endpoints_in_json \ "post" \ "content" \  "entity")).replace("\"","")) //returns two different entity locations, trimming it down!
      Endpoints.setOauthToken(JsonExtractor.extract(endpoints_in_json, "oauth_token"))
      Endpoints.setNewPost(JsonExtractor.extract(endpoints_in_json, "new_post"))
      Endpoints.setPostFeed(JsonExtractor.extract(endpoints_in_json, "posts_feed"))
      Endpoints.setPost(compact(render(endpoints_in_json \ "post" \ "content" \ "servers" \ "urls" \ "post")).replace("\"",""))
      Endpoints.setAttachment(JsonExtractor.extract(endpoints_in_json, "attachment"))
      Endpoints.setPostAttachment(JsonExtractor.extract(endpoints_in_json, "post_attachment"))
      Endpoints.setBatch(JsonExtractor.extract(endpoints_in_json, "batch"))
      Endpoints.setServerInfo(JsonExtractor.extract(endpoints_in_json, "server_info"))


    }

    def register() {

      val postLocationHeader = HttpRequest.post(Endpoints.getNewPost).contentType("application/vnd.tent.post.v0+json; type=\"https://tent.io/types/app/v0#\"").send(identityJson.registration).header("Link")

      val postLocation = postLocationHeader.split("<")(1).split(">")(0)

      val jsonHawkCredentials = parse(HttpRequest.get(postLocation).accept("application/vnd.tent.post.v0+json").body)

      TempCredentials.setHawkAlgorithm(JsonExtractor.extract(jsonHawkCredentials, "hawk_algorithm"))
      TempCredentials.setHawkKey(JsonExtractor.extract(jsonHawkCredentials, "hawk_key"))
      //Client_ID must be stored and is not a temporary thing, others are
      Credentials.setClientID(compact(render(jsonHawkCredentials \ "post" \ "mentions" \"post")).replace("\"",""))
      TempCredentials.setHawkID(compact(render(jsonHawkCredentials \ "post" \ "id")).replace("\"",""))  //I can extract this /nicer/ with the methods in lift-json, but I'm lazy right now
    }

    def Auth() {


      val url = new URL(Endpoints.getOauthAuth + "?client_id=" + Credentials.getClientID + "&state=" + state)
      val connection = url.openConnection().asInstanceOf[HttpURLConnection]
      connection.setInstanceFollowRedirects(false)
      val location = connection.getHeaderField("Location")  //HAH! On computer, HttpUrlConnection does not follow redirects. Android does. HttpRequest, which is based on HttpUrlconnection, can't turn off redirects
      val code = location.split("code=")(1).split("&state=")(0)

      val json = "{\n  \"code\": \"%s\",\n  \"token_type\": \"https://tent.io/oauth/hawk-token\"\n}".format(code)

      val jsonResponse = parse(HttpRequest.post(Endpoints.getOauthToken).accept("application/json").authorization(Hawk_Headers.build_headers(json,"POST",Endpoints.getOauthToken, true)).contentType("application/json").send(json).body)
      Credentials.setAccessToken(JsonExtractor.extract(jsonResponse,"access_token"))
      Credentials.setHawkAlgorithm(JsonExtractor.extract(jsonResponse,"hawk_algorithm"))
      Credentials.setHawkKey(JsonExtractor.extract(jsonResponse,"hawk_key"))
      Credentials.setTokenType(JsonExtractor.extract(jsonResponse,"token_type"))

    }

    extractEndpoint()
    register()
    Auth()
    true


  }


}
