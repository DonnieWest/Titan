package com.donniewest.titan.Util

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import com.github.kevinsawicki.http.HttpRequest.Base64
import java.security.MessageDigest

object Sign {

  def Base64_and_HmacSha256(to_sign: String, mac_key: String) = {

    val mac = Mac.getInstance("HmacSHA256")
    mac.init(new SecretKeySpec(mac_key.getBytes,"HmacSHA256"))
    mac.update(to_sign.getBytes)
    val res = mac.doFinal()
    Base64.encodeBytes(res)

  }

  def Base64_and_Sha256_Digest(to_sign: String) = {

    val message_digester = MessageDigest.getInstance("SHA-256")
    Base64.encodeBytes(message_digester.digest(to_sign.getBytes))


  }

}