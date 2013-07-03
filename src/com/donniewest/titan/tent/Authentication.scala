package tent

import net.liftweb.json._
import com.github.kevinsawicki.http.HttpRequest
import scala.util.Random
import java.net.{HttpURLConnection, URL}
import com.donniewest.titan.Util.json_extractor

object Authentication {

  def login(entity: String) {

    lazy val state = {
      val random = new Random().nextInt()
      if (random < 0) -random else random
    }
    //Need to find API endpoint
    val server_endpoint = entity + HttpRequest.head(entity).header("Link").split("<")(1).split(">")(0)

    def extract_endpoints() {

      //takes in the server from find_server, extracts the endpoints for me to post and authenticate + etc

      val endpoints_in_json = parse(HttpRequest.get(server_endpoint).accept("application/vnd.tent.post.v0+json").body())
      Endpoints.setOauth_auth(json_extractor.extract(endpoints_in_json, "oauth_auth"))
      Endpoints.setEntity(compact(render(endpoints_in_json \ "post" \ "content" \  "entity")).replace("\"","")) //returns two different entity locations, trimming it down!
      Endpoints.setOauth_token(json_extractor.extract(endpoints_in_json, "oauth_token"))
      Endpoints.setNew_post(json_extractor.extract(endpoints_in_json, "new_post"))
      Endpoints.setPost_feed(json_extractor.extract(endpoints_in_json, "posts_feed"))
      Endpoints.setPost_feed(compact(render(endpoints_in_json \ "post" \ "content" \ "servers" \ "urls" \ "post")).replace("\"",""))
      Endpoints.setAttachment(json_extractor.extract(endpoints_in_json, "attachment"))
      Endpoints.setPost_attachment(json_extractor.extract(endpoints_in_json, "post_attachment"))
      Endpoints.setBatch(json_extractor.extract(endpoints_in_json, "batch"))
      Endpoints.setServer_info(json_extractor.extract(endpoints_in_json, "server_info"))


    }

    def register() {

      val post_location_header = HttpRequest.post(Endpoints.getNew_post).contentType("application/vnd.tent.post.v0+json; type=\"https://tent.io/types/app/v0#\"").send(identity_json.registration).header("Link")

      val post_location = post_location_header.split("<")(1).split(">")(0)

      val json_hawk_creds = parse(HttpRequest.get(post_location).accept("application/vnd.tent.post.v0+json").body)

      Temp_Credentials.setHawk_algorithm(json_extractor.extract(json_hawk_creds, "hawk_algorithm"))
      Temp_Credentials.setHawk_key(json_extractor.extract(json_hawk_creds, "hawk_key"))
      Temp_Credentials.setClient_id(compact(render(json_hawk_creds \ "post" \ "mentions" \"post")).replace("\"",""))
      Temp_Credentials.setHawk_id(compact(render(json_hawk_creds \ "post" \ "id")).replace("\"",""))  //I can extract this /nicer/ with the methods in lift-json, but I'm lazy right now
    }

    def Auth() {


      val locate = new URL(Endpoints.getOauth_auth + "?client_id=" + Temp_Credentials.getClient_id + "&state=" + state)
      val located = locate.openConnection().asInstanceOf[HttpURLConnection]
      located.setInstanceFollowRedirects(false)
      val location = located.getHeaderField("Location")  //HAH! On computer, HttpUrlConnection does not follow redirects. Android does. HttpRequest, which is based on HttpUrlconnection, can't turn off redirects
      val code = location.split("code=")(1).split("&state=")(0)

      val json = "{\n  \"code\": \"%s\",\n  \"token_type\": \"https://tent.io/oauth/hawk-token\"\n}".format(code)

      val json_response = parse(HttpRequest.post(Endpoints.getOauth_token).accept("application/json").authorization(Hawk_Headers.build_headers(json,"POST",Endpoints.getOauth_token)).contentType("application/json").send(json).body)
      Credentials.setAccess_token(json_extractor.extract(json_response,"access_token"))
      Credentials.setHawk_algorithm(json_extractor.extract(json_response,"hawk_algorithm"))
      Credentials.setHawk_key(json_extractor.extract(json_response,"hawk_key"))
      Credentials.setToken_type(json_extractor.extract(json_response,"token_type"))

    }

    extract_endpoints()
    register()
    Auth()
    true


  }


}
