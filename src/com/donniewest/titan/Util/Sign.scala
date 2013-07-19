package Util

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import com.github.kevinsawicki.http.HttpRequest.Base64
import java.security.MessageDigest

object Sign {

  def base64andHmacSha256(to_sign: String, mac_key: String) = {

    val mac = Mac.getInstance("HmacSHA256")
    mac.init(new SecretKeySpec(mac_key.getBytes,"HmacSHA256"))
    mac.update(to_sign.getBytes)
    val res = mac.doFinal()
    Base64.encodeBytes(res)

  }

  def base64AndSha256Digest(to_sign: String) = {

    val digest = MessageDigest.getInstance("SHA-256")
    Base64.encodeBytes(digest.digest(to_sign.getBytes))


  }

  def RandomKeyGen() = {




  }

}