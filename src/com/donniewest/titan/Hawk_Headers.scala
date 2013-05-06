package com.donniewest.titan

import scala.util.Random
import com.donniewest.titan.Util.Sign

object Hawk_Headers {

  def build_headers(body: String, method: String, url: String) = {

    assert(method == "POST" | "GET" | "HEAD" | "PUT" | "DELETE")

    val hawk_id = Credentials.getHawk_id
    val app = Credentials.getClient_id
    val nonce = Random.alphanumeric.take(8).mkString
    val timestamp =  (System.currentTimeMillis() / 1000).asInstanceOf[Int]

    val path = "" //need to extract this from url
    val host = "" //need to extract this from url (Maybe cast as type url and pull these from this with nice methods?"
    val app_digest = "" //dunno what an app digest is, but let's figure it out!

    val hash = Sign.Base64_and_HmacSha256("hawk.1.payload\napplication/json\n" + body)
    val mac = Sign.Base64_and_HmacSha256(s"hawk.1.auth\n$timestamp\n$method\n$path\n$host\n443\n$hash\n$app\n$app_digest")

    s"Hawk id=\"$hawk_id\", mac=\"$mac\", ts=\"$timestamp\", nonce=\"$nonce\", hash=\"$hash\", app=\"$app\""


  }


}
