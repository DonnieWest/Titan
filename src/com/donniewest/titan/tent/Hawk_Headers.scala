package tent

import scala.util.Random
import Util.Sign
import java.net.URL

object Hawk_Headers {

  def build_headers(
    body: String,
    method: String,
    url: String,
    isTemporary: Boolean = false,
    contentType: String = "application/json") = {

    lazy val hawkID = if (isTemporary) TempCredentials.getHawkID else Credentials.getAccessToken   //access token is used as Hawk_Id in requests after auth
    lazy val hawkKey =  if (isTemporary) TempCredentials.getHawkKey else Credentials.getHawkKey
    val app = Credentials.getClientID   //I believe the Client_ID from earlier still defines this app. I need to store this in database as well
    val nonce = Random.alphanumeric.take(8).mkString
    val timestamp =  (System.currentTimeMillis() / 1000).asInstanceOf[Int]
    val castedUrl = new URL(url)   //Aha! Instantiate new URL, get pretty methods, type safe!
    // Since the "path" here rips out the query parts of the  URL, here is where we add them back into a full request Uri
    val containsQuery = if (castedUrl.getQuery != null) true else false
    val URI = if (containsQuery) castedUrl.getPath + "?" + castedUrl.getQuery  else castedUrl.getPath
    val host = castedUrl.getHost


    val hash = Sign.base64AndSha256Digest("hawk.1.payload\n%s\n%s\n".format(contentType, body))
    val mac = Sign.base64andHmacSha256("hawk.1.header\n%d\n%s\n%s\n%s\n%s\n80\n%s\n\n%s\n\n".format(timestamp, nonce, method, URI, host, hash, app), hawkKey)
    "Hawk id=\"%s\", mac=\"%s\", ts=\"%d\", nonce=\"%s\", hash=\"%s\", app=\"%s\"".format(hawkID, mac, timestamp, nonce, hash, app)

  }

}
