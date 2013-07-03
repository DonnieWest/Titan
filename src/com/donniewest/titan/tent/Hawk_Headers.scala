package tent

import scala.util.Random
import Util.Sign
import java.net.URL

object Hawk_Headers {

  def build_headers(body: String, method: String, url: String) = {

    val hawk_id = Temp_Credentials.getHawk_id
    val app = Temp_Credentials.getClient_id
    val nonce = Random.alphanumeric.take(8).mkString
    val timestamp =  (System.currentTimeMillis() / 1000).asInstanceOf[Int]
    val url_casted = new URL(url)   //Aha! Instantiate new URL, get pretty methods, type safe!
    val path = (if (url_casted.getQuery != null) (url_casted.getPath + "?" + url_casted.getQuery) else url_casted.getPath)
    val host = url_casted.getHost

    val hash = Sign.Base64_and_Sha256_Digest("hawk.1.payload\napplication/json\n" + body)
    val mac = Sign.Base64_and_HmacSha256("hawk.1.header\n%d\n%s\n%s\n%s\n%s\n80\n%s\n\n%s\n\n".format(timestamp, nonce, method, path, host, hash, app), Temp_Credentials.getHawk_key) //according to the ruby library for Hawk for Tent, app id and app id digest aren't used here.

    "Hawk id=\"%s\", mac=\"%s\", ts=\"%d\", nonce=\"%s\", hash=\"%s\", app=\"%s\"".format(hawk_id, mac, timestamp, nonce, hash, app)

  }


  def build_headers_after_authentication(body: String, method: String, url: String, contenttype: String) = {


    val hawk_id = Credentials.getAccess_token //access token is used as Hawk_Id in requests after auth
    val app = Temp_Credentials.getClient_id   //I believe the Client_ID from earlier still defines this app. I need to store this in database as well
    val nonce = Random.alphanumeric.take(8).mkString
    val timestamp =  (System.currentTimeMillis() / 1000).asInstanceOf[Int]
    val url_casted = new URL(url)   //Aha! Instantiate new URL, get pretty methods, type safe!
    val path = (if (url_casted.getQuery != null) (url_casted.getPath + "?" + url_casted.getQuery) else url_casted.getPath)
    val host = url_casted.getHost

    val hash = Sign.Base64_and_Sha256_Digest("hawk.1.payload\n%s\n%s\n".format(contenttype, body))
    val mac = Sign.Base64_and_HmacSha256("hawk.1.header\n%d\n%s\n%s\n%s\n%s\n80\n%s\n\n%s\n\n".format(timestamp, nonce, method, path, host, hash, app), Credentials.getHawk_key)
    "Hawk id=\"%s\", mac=\"%s\", ts=\"%d\", nonce=\"%s\", hash=\"%s\", app=\"%s\"".format(hawk_id, mac, timestamp, nonce, hash, app)

    //TODO: Fix this so it handles queries in the URL
  }

}
