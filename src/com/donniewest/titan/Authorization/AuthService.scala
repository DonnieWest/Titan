package com.donniewest.titan.Authorization

import com.donniewest.titan.TentAuth
import android.content.Intent
import android.app.IntentService
import android.util.Log

class AuthService(stringy: String) extends IntentService(stringy: String) {


  def this() = this("MyAuthService")

  override def onHandleIntent(intent: Intent) {

    val data = intent.getData
    val path = data.getPathSegments
    TentAuth.auth(path.get(0))
    Log.e("Service", "Service finished!")

  }


}

