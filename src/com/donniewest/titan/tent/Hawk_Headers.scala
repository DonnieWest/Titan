package tent

import scala.util.Random
import Util.Sign
import java.net.URL

object Hawk_Headers {

  def build_headers(body: String, method: String, url: String, is_temporary: Boolean = false, content_type: String = "application/json") = {

    lazy val hawk_id = if (is_temporary) Temp_Credentials.getHawk_id else Credentials.getAccess_token   //access token is used as Hawk_Id in requests after auth
    lazy val hawk_key =  if (is_temporary) Temp_Credentials.getHawk_key else Credentials.getHawk_key
    val app = Credentials.getClient_id   //I believe the Client_ID from earlier still defines this app. I need to store this in database as well
    val nonce = Random.alphanumeric.take(8).mkString
    val timestamp =  (System.currentTimeMillis() / 1000).asInstanceOf[Int]
    val url_casted = new URL(url)   //Aha! Instantiate new URL, get pretty methods, type safe!
    // Since the "path" here rips out the query parts of the  URL, here is where we add them back into a full request Uri
    val contains_query = if (url_casted.getQuery != null) true else false
    val URI = if (contains_query) url_casted.getPath + "?" + url_casted.getQuery  else url_casted.getPath
    val host = url_casted.getHost


    val hash = Sign.Base64_and_Sha256_Digest("hawk.1.payload\n%s\n%s\n".format(content_type, body))
    val mac = Sign.Base64_and_HmacSha256("hawk.1.header\n%d\n%s\n%s\n%s\n%s\n80\n%s\n\n%s\n\n".format(timestamp, nonce, method, URI, host, hash, app), hawk_key)
    "Hawk id=\"%s\", mac=\"%s\", ts=\"%d\", nonce=\"%s\", hash=\"%s\", app=\"%s\"".format(hawk_id, mac, timestamp, nonce, hash, app)

    //TODO: Fix this so it handles queries in the URL
  }

}
