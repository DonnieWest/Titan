package com.donniewest.titan

import com.github.kevinsawicki.http.HttpRequest
import com.github.kevinsawicki.http.HttpRequest.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import scala.beans.BeanProperty
import util.Random
import android.util.Log

object Creds {

  @BeanProperty var id: String = ""
  @BeanProperty var reg_mac_id: String = ""
  @BeanProperty var reg_mac_key: String = ""
  @BeanProperty var reg_mac_alg: String = ""
  @BeanProperty var auth_location: String = ""
  @BeanProperty var host: String = ""
  @BeanProperty var code: String = ""
  @BeanProperty var access_token: String = ""
  @BeanProperty var refresh_token: String = ""
  @BeanProperty var auth_mac_key: String = ""
  @BeanProperty var auth_mac_alg: String = ""
  @BeanProperty var expires: String = ""
  @BeanProperty var current_timestamp = 0

  def timestamp = setCurrent_timestamp((System.currentTimeMillis() / 1000).asInstanceOf[Int])


  def generate_authheader(request_uri: String, method: String) = {
    Log.e("AuthHeaders", "Entering generate authHeader")
    val nonce = {

// got this from https://gist.github.com/mahata/4145905

      Stream.continually(util.Random.nextPrintableChar) take 6 mkString
    }

    def mac = {


    val sign_this = {
      Log.e("AuthHeaders", "Entering sign this")
      val host = Creds.getHost
      Log.e("AuthHeaders", "timestamp, nonce, method, etc are " + getCurrent_timestamp + " " + nonce + " " + method + " " + android.net.Uri.encode(request_uri) + " " + android.net.Uri.encode(host))
      getCurrent_timestamp + "\n" + nonce + "\n" + method + "\n" + android.net.Uri.encode("/tent/" + request_uri) + "\n" + android.net.Uri.encode(host) + "\n" + "443" + "\n" + "" + "\n" + "" + "\n"


    }

    Log.e("AuthHeaders", "sign this is " + sign_this)
    Log.e("AuthHeaders", "Mac key is " + Creds.getReg_mac_key + " and Mac Algorithm is " + Creds.getReg_mac_alg)
    val m = Mac.getInstance("HmacSHA256")
    m.init(new SecretKeySpec(Creds.getReg_mac_key.getBytes,"HmacSHA256"))
    m.update(sign_this.getBytes)
    val res = m.doFinal()
//    android.util.Base64.encodeToString(res, android.util.Base64.URL_SAFE)
    Base64.encodeBytes(res)

/*    val keyspec = new SecretKeySpec(Creds.getReg_mac_key.getBytes,"HmacSHA256")
    val instance = Mac.getInstance("HmacSHA256")
    instance.init(keyspec)
    Base64.encode(instance.doFinal(sign_this.getBytes))*/

    }

  Log.e("AuthHeaders", "The mac is " + mac)

  val auth_header = s"""MAC id=$getReg_mac_id, ts="$getCurrent_timestamp", nonce="$nonce", mac="$mac""""
  Log.e("AuthHeaders","The mac is " +  mac)
  auth_header
  }

  val state = {
    val random = new Random()
    val randomized = random.nextInt()
    if (randomized < 0) -randomized else randomized
  }
  val redirect_uri = "titan://oath"
  val content = "application/vnd.tent.v0+json"
  def send_signed_json(url: String, json: String) = {
    Log.e("AuthHeaders", "The auth location is:" + getAuth_location.toString + url)
    val authorization_headers = generate_authheader(url, "POST")
    Log.e("AuthHeaders", "The auth header is:" + authorization_headers)
    HttpRequest.post(getAuth_location + url).accept(Creds.content).contentType(Creds.content).authorization(authorization_headers).send(json).body()
}

  def get_signed_body(url: String) = {
    Log.e("AuthHeaders", "Entering signed body")
    val auth_headers = generate_authheader(url, "GET")
    Log.e("AuthHeaders", auth_headers)
    HttpRequest.get(getAuth_location + url).accept(Creds.content).contentType(Creds.content).authorization(auth_headers).body


  }

  val json =
    """
{
  "name": "Titan",
  "description": "Social Networking Client for Tent to view your Feed, Friends and Profile",
  "url": "http://github.com/Titan",
  "icon": "http://example.com/icon.png",
  "redirect_uris": [
    "titan://oath"
  ],
  "scopes": {
    "write_profile": "Allows user to view and modify profile information",
    "write_followings": "Allows user to view and modify followings",
    "write_followers": "Allows user to view and modify followers",
    "write_posts": "Allows user to view and modify posts"
  }
}
    """

}
