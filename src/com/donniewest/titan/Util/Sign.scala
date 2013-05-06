package com.donniewest.titan.Util

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import com.github.kevinsawicki.http.HttpRequest.Base64

object Sign {

  def Base64_and_HmacSha256(to_sign: String, mac_key: String) = {

    val mac = Mac.getInstance("HmacSHA256")
    mac.init(new SecretKeySpec(mac_key.getBytes,"HmacSHA256"))
    mac.update(to_sign.getBytes)
    val res = mac.doFinal()
    Base64.encodeBytes(res)

  }

}