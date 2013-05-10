package com.donniewest.titan

import scala.util.Random
import com.donniewest.titan.Util.Sign
import java.net.URL

object Hawk_Headers {

  def build_headers(body: String, method: String, url: String) = {

    val hawk_id = Credentials.getHawk_id
    val app = Credentials.getClient_id
    val nonce = Random.alphanumeric.take(8).mkString
    val timestamp =  (System.currentTimeMillis() / 1000).asInstanceOf[Int]
    val url_casted = new URL(url)   //Aha! Instantiate new URL, get pretty methods, type safe!
    val path = url_casted.getPath
    val host = url_casted.getHost
//    val app_digest = "" //doesn't seem to be required. Remove later?

    val hash = Sign.Base64_and_HmacSha256("hawk.1.payload\napplication/json\n" + body, Credentials.getHawk_key)
    val mac = Sign.Base64_and_HmacSha256("hawk.1.header\n%d\n%s\n%s\n%s\n%s\n80\n%s\n\n%s\n\n".format(timestamp, nonce, method, path, host, hash, app), Credentials.getHawk_key) //according to the ruby library for Hawk for Tent, app id and app id digest aren't used here.

    "Hawk id=\"%s\", mac=\"%s\", ts=\"%d\", nonce=\"%s\", hash=\"%s\", app=\"%s\"".format(hawk_id, mac, timestamp, nonce, hash, app)

  }


  def build_headers_for_authentication(body: String, method: String, url: String) = {


    val hawk_id = Temporary_Credentials.getAccess_token //access token is used as Hawk_Id in requests after auth
    val app = Credentials.getClient_id   //I believe the Client_ID from earlier still defines this app. I need to store this in database as well
    val nonce = Random.alphanumeric.take(8).mkString
    val timestamp =  (System.currentTimeMillis() / 1000).asInstanceOf[Int]
    val url_casted = new URL(url)   //Aha! Instantiate new URL, get pretty methods, type safe!
    val path = url_casted.getPath
    val host = url_casted.getHost

    val hash = Sign.Base64_and_HmacSha256("hawk.1.payload\napplication/json\n" + body, Temporary_Credentials.getHawk_key)
    val mac = Sign.Base64_and_HmacSha256("hawk.1.header\n%d\n%s\n%s\n%s\n%s\n80\n%s\n\n%s\n\n".format(timestamp, nonce, method, path, host, hash, app), Temporary_Credentials.getHawk_key) //according to the ruby library for Hawk for Tent, app id and app id digest aren't used here.

    "Hawk id=\"%s\", mac=\"%s\", ts=\"%d\", nonce=\"%s\", hash=\"%s\", app=\"%s\"".format(hawk_id, mac, timestamp, nonce, hash, app)

  }

}
