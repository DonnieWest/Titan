package com.donniewest.titan

import com.github.kevinsawicki.http.HttpRequest
import com.github.kevinsawicki.http.HttpRequest.Base64
import java.security.SecureRandom
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import scala.beans.BeanProperty
import util.Random

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

    def create_timestamp = System.currentTimeMillis() / 1000
    def nonce = {
      val seed = SecureRandom.getInstance("SHA1PRNG")
      seed.nextInt(102943)
      val random2 = SecureRandom.getInstance("SHA1PRNG")
      random2.setSeed(seed.generateSeed(10))
      random2.asInstanceOf[Int]
    }
    val timestamp = create_timestamp
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
    }
    val sign_this = {
      val host = Creds.getHost
      "$timestamp/n$nonce/n$method/n$url/n$host/n443/n /n".format(timestamp, nonce, method, request_uri, host)


    }

    val keyspec = new SecretKeySpec(Creds.getAuth_mac_key.getBytes(), Creds.getAuth_mac_alg)
    val instance = Mac.getInstance(Creds.getAuth_mac_alg)
    instance.init(keyspec)
    Base64.encodeBytes(instance.doFinal(sign_this.getBytes))



  val auth_header = "MAC id=" + Creds.getReg_mac_id + ", ts=" + timestamp + ", nonce=" + nonce + ", mac=" + mac
  setCurrent_timestamp(timestamp.asInstanceOf[Int])
  auth_header
  }

  val state = {
    val random = new Random()
    val randomized = random.nextInt()
    if (randomized < 0) -randomized else randomized
  }
  val redirect_uri = /*"titan://success"*/ "titan://oath"
  val content = "application/vnd.tent.v0+json"
  def send_signed_json(url: String, json: String) = {

    HttpRequest.post(url).accept(Creds.content).contentType(Creds.content).authorization(generate_authheader(url, "POST")).send(json).body
}

  def get_signed_body(url: String) = {

    HttpRequest.get(url).accept(Creds.content).authorization(generate_authheader(url, "GET")).body


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
