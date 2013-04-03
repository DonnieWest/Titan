package com.donniewest.titan

import com.github.kevinsawicki.http.HttpRequest
import com.github.kevinsawicki.http.HttpRequest.Base64
import java.security.SecureRandom
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

  def generate_authheader(request_uri: String, method: String) = {
    Log.e("AuthHeaders", "Entering generate authHeader")
    def create_timestamp = System.currentTimeMillis() / 1000
    def nonce : Int = {
      Log.e("AuthHeaders", "Generating Nonce")
      val seed = SecureRandom.getInstance("SHA1PRNG")
      Log.e("AuthHeaders", "Generating Seed")
      seed.nextInt(102943)
      Log.e("AuthHeaders", "Next int on Seed")
      val random2 = SecureRandom.getInstance("SHA1PRNG")
      Log.e("AuthHeaders", "Random2 generations")
      random2.setSeed(seed.generateSeed(10))
      Log.e("AuthHeaders", "Setting seed")
      val nonciated = random2.nextInt()   // here is where I casted it to a string. Not working.
      Log.e("AuthHeaders", "Finishing up nonce!")
      Log.e("AuthHeaders", "Nonce is " + nonciated.toString)
      if (nonciated < 0) -nonciated else nonciated
    }
    Log.e("AuthHeaders", "nonce is " + nonce.toString)
    val timestamp = create_timestamp
    Log.e("AuthHeaders", "timestamp is " + timestamp.toString)

    def mac = {
      /*
      val sign_this = {
        timestamp + "/n" +
        nonce + "/n" +
        "POST" + "/n" +
        "/apps/" + Creds.getId + "/authorizations" + "/n" +
        Creds.getHost + "/n" +
        "443" + "/n"  +
        ""  + "/n"
        **/

    val sign_this = {
      Log.e("AuthHeaders", "Entering sign this")
      val host = Creds.getHost
      Log.e("AuthHeaders", "timestamp, nonce, method, etc are " + timestamp + " " + nonce + " " + method + " " + request_uri + " " + host)
      "$timestamp/n$nonce/n$method/n$url/n$host/n443/n /n".format(timestamp, nonce, method, request_uri, host)


    }

    val keyspec = new SecretKeySpec(Creds.getAuth_mac_key.getBytes(), Creds.getAuth_mac_alg)
    Log.e("AuthHeaders", "Keyspec is " + keyspec.toString)
    val instance = Mac.getInstance(Creds.getAuth_mac_alg)
    instance.init(keyspec)
    Base64.encodeBytes(instance.doFinal(sign_this.getBytes))

    }

  Log.e("AuthHeaders", "The mac is " + mac)

  val auth_header = "MAC id=" + Creds.getReg_mac_id + ", ts=" + timestamp + ", nonce=" + nonce + ", mac=" + mac
  Log.e("AuthHeaders","The mac is " +  mac)
  setCurrent_timestamp(timestamp.asInstanceOf[Int])
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

    HttpRequest.post(getAuth_location + url).accept(Creds.content).contentType(Creds.content).authorization(generate_authheader(url, "POST")).send(json).body
}

  def get_signed_body(url: String) = {
    Log.e("AuthHeaders", "Entering signed body")
    val auth_headers = generate_authheader(url, "GET")
    Log.e("AuthHeaders", auth_headers)
    HttpRequest.get(url).accept(Creds.content).contentType(Creds.content).authorization(auth_headers).body


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
