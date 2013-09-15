package com.donniewest.titan.tent

import net.liftweb.json._
import com.github.kevinsawicki.http.HttpRequest
import scala.util.Random
import java.net.{HttpURLConnection, URL}

object Authentication {

  def one_leg_login(entity: String) {

/*    takes given entity and does the first steps necessary to Authenticate with Tent Server
    1. Discovery (extractEndpoints)
    2. Registration
*/


    //Need to find API endpoint
    val serverEndpoint = entity + HttpRequest.head(entity).header("Link").split("<")(1).split(">")(0)

    def extractEndpoint() {
      implicit val formats = DefaultFormats
          
      //takes in the server from serverEndpoint, extracts the endpoints for me to post and authenticate + etc

      val endpoints_in_json = parse(HttpRequest.get(serverEndpoint).accept("application/vnd.tent.post.v0+json").body())
      
      val translated = endpoints_in_json \ "content" \ "servers" \ "urls" 

      case class urls(val oauth_auth: String,
                      val oauth_token: String,
                      val posts_feed: String,
                      val post: String,
                      val new_post: String,
                      val post_attachment: String,
                      val attachment: String,
                      val batch: String,
                      val server_info: String,
                      val discover: String)

      val all_endpoints = translated.extract[urls]
        
      Endpoints.oauthAuth(all_endpoints.oauth_auth)
      Endpoints.entity(compact(render(endpoints_in_json \ "post" \ "content" \  "entity")).replace("\"","")) //returns two different entity locations, trimming it down!
      Endpoints.oauthToken(all_endpoints.oauth_token)
      Endpoints.newPost(all_endpoints.new_post)
      Endpoints.postFeed(all_endpoints.posts_feed)
      Endpoints.post(((all_endpoints.post).replace("\"",""))
      Endpoints.attachment(all_endpoints.attachment)
      Endpoints.postAttachment(all_endpoints.post_attachment)
      Endpoints.batch(all_endpoints.batch)
      Endpoints.serverInfo(all_endpoints.server_info)

      //TODO: Convert to a case class, properly parse all this into that!


    }

    def register() {

//     registers with tent server, gets temporary credentials for next step

      val postLocationHeader = HttpRequest.post(Endpoints.newPost).contentType("application/vnd.tent.post.v0+json; type=\"https://tent.io/types/app/v0#\"").send(identityJson.registration).header("Link")

      val postLocation = postLocationHeader.split("<")(1).split(">")(0)

      val jsonHawkCredentials = parse(HttpRequest.get(postLocation).accept("application/vnd.tent.post.v0+json").body)

      TempCredentials.setHawkAlgorithm(JsonExtractor.extract(jsonHawkCredentials, "hawk_algorithm"))
      TempCredentials.setHawkKey(JsonExtractor.extract(jsonHawkCredentials, "hawk_key"))
      //Client_ID must be stored and is not a temporary thing, others are
      Credentials.setClientID(compact(render(jsonHawkCredentials \ "post" \ "mentions" \"post")).replace("\"",""))
      TempCredentials.setHawkID(compact(render(jsonHawkCredentials \ "post" \ "id")).replace("\"",""))  //I can extract this /nicer/ with the methods in lift-json, but I'm lazy right now
    }


//    1. Discovers

    extractEndpoint()

//    2. Registers

    register()


  }


  def second_leg_Auth() {

//      Authenticates with server and retrieves permanent credentials for posting and etc
      lazy val state = {
        val random = new Random().nextInt()
      if (random < 0) -random else random
      }

      val url = new URL(Endpoints.oauthAuth + "?client_id=" + Credentials.getClientID + "&state=" + state)
      val connection = url.openConnection().asInstanceOf[HttpURLConnection]
      connection.setInstanceFollowRedirects(false)
      val location = connection.getHeaderField("Location")  //HAH! On computer, HttpUrlConnection does not follow redirects. Android does. HttpRequest, which is based on HttpUrlconnection, can't turn off redirects
      val code = location.split("code=")(1).split("&state=")(0)

      val json = "{\n  \"code\": \"%s\",\n  \"token_type\": \"https://tent.io/oauth/hawk-token\"\n}".format(code)

      val jsonResponse = parse(HttpRequest.post(Endpoints.oauthToken).accept("application/json").authorization(Hawk_Headers.build_headers(json,"POST",Endpoints.oauthToken, true, "application/json")).contentType("application/json").send(json).body)
      Credentials.setAccessToken(JsonExtractor.extract(jsonResponse,"access_token"))
      Credentials.setHawkAlgorithm(JsonExtractor.extract(jsonResponse,"hawk_algorithm"))
      Credentials.setHawkKey(JsonExtractor.extract(jsonResponse,"hawk_key"))
      Credentials.setTokenType(JsonExtractor.extract(jsonResponse,"token_type"))

    }


  }



