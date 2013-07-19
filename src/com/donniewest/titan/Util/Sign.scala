package Util

import javax.crypto.{KeyGenerator, Mac}
import javax.crypto.spec.SecretKeySpec
import com.github.kevinsawicki.http.HttpRequest.Base64
import java.security.{SecureRandom, MessageDigest}

object Sign {

  def base64andHmacSha256(toSign: String, macKey: String) = {

//    takes in something to sign and a key, creates a mac using HmacSha256 and encodes it in base64. Returns mac

    val mac = Mac.getInstance("HmacSHA256")
    mac.init(new SecretKeySpec(macKey.getBytes,"HmacSHA256"))
    mac.update(toSign.getBytes)
    val res = mac.doFinal()
    Base64.encodeBytes(res)

  }

  def base64AndSha256Digest(toSign: String) = {

//    takes in something to sign, creates a Sha256 digest encoded in Base64 and returns it

    val digest = MessageDigest.getInstance("SHA-256")
    Base64.encodeBytes(digest.digest(toSign.getBytes))


  }

  def randomKeyGen() = {

/*    ripped from here: http://android-developers.blogspot.com/2013/02/using-cryptography-to-store-credentials.html
    generates and returns a random Key*/


    val outputKeyLength = 256
    val secureRandom = new SecureRandom()
    val keyGenerator = KeyGenerator.getInstance("AES")
    keyGenerator.init(outputKeyLength, secureRandom)
    val key = keyGenerator.generateKey()
    key


  }

}