package com.donniewest.titan

import com.github.kevinsawicki.http.HttpRequest
import net.liftweb.json._
import com.donniewest.titan.Creds
import android.util.Log

object TentAuth {

  def redirect_url(url: String) = {

    def Register(url: String) {
      Log.d("Register", "#1 Entering Register!")
      implicit val formats = net.liftweb.json.DefaultFormats
      val request = HttpRequest.head(url).contentType(Creds.content).acceptEncoding(Creds.content)
      val tent_headers = request.header("Link")
      Log.d("Register", "#2" + tent_headers)
      val end = tent_headers.indexOf(">")
      Log.d("Register", end.toString)
      Creds.setAuth_location(tent_headers.slice(1, (end - 7)))
      Log.d("Register", "#3" + Creds.getAuth_location)
      Creds.setHost(request.getConnection.getURL.getHost)
      Log.d("Register", "#4" + Creds.getHost)
      val json_request = parse(HttpRequest.post(Creds.getAuth_location + "apps").acceptEncoding(Creds.content).contentType(Creds.content).send(Creds.json).body())
      Log.d("Register", "#5" + json_request.toString)
      val identifier = (compact(render((json_request \\ "id"))))
      val length = identifier.length - 1
      Creds.setId(identifier.slice(1, length))
      Log.d("Register", "#6 Apparently pulling out from the Json_request works if I am seeing this...")
      Creds.setReg_mac_key(compact(render((json_request \\ "mac_key"))))
      Creds.setReg_mac_id(compact(render((json_request \\ "mac_key_id"))))
      Creds.setReg_mac_alg(compact(render((json_request \\ "mac_algorithm"))))
      Log.d("Register", "#7 All finished, let's move on!")

    }

    def build_url = {
      (Creds.getAuth_location +
        "oauth/authorize?client_id=" + Creds.getId +
        "&redirect_uri=" + Creds.redirect_uri +
        "&scope=write_profile,write_followings,write_followers,write_posts" +
        "&state=" + Creds.state +
        "&tent_profile_info_types=all" +
        "&tent_post_types=all")


    }
    Register(url)
    build_url

  }


  def auth(code: String) = {

    Creds.setCode(code)
    Creds.timestamp
    val uri = "apps/" + Creds.getId + "/authorizations"
    val auth_json = """   {
                        "code": """" + Creds.getCode + """",
                        "token_type": "mac",
                        "tent_expires_at": """ + (Creds.getCurrent_timestamp + 86400) + """
                      }

                                                                                        """
    Log.e("Authorized!", auth_json + "uri also is " + uri)
    Log.e("Authorized!", "Your code is " + Creds.getCode)
    val response_body = Creds.send_signed_json(uri, auth_json).toString
    Log.e("Authorized!", "After sending the signed json, the response body is " + response_body)
    val json_response = parse(response_body)
    Creds.setAuth_mac_key(compact(render(json_response \\ "mac_key")))
    Creds.setAuth_mac_alg(compact(render(json_response \\ "mac_algorithm")))
    Creds.setRefresh_token(compact(render(json_response \\ "refresh_token")))
    Creds.setExpires(compact(render(json_response \\ "tent_expires_at")))

    Log.e("Authorized!", "success!")
    Log.e("Authorized!", "Your mac key is " + Creds.getAuth_mac_key)
    Log.e("Authorized!", "Your refresh token is " + Creds.getRefresh_token)
  }
}
