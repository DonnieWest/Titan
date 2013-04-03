package com.donniewest.titan

import com.actionbarsherlock.app.SherlockActivity
import com.donniewest.titan.R
import com.donniewest.titan.TentAuth
import android.widget.{TextView, Button}
import org.scaloid.common._
import android.os.Bundle
import scala.concurrent.ops._
import android.util.Log
import android.content.Intent

class LoginActivity extends SherlockActivity with SActivity {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.login_activity)
    find[Button](R.id.b_signin).onClick {

      spawn{

        val hurl = find[TextView](R.id.et_password).getText.toString
        warn(hurl)
/*        TentAuth.Register(hurl)
        warn("Done with Registration!")
        val redirection = TentAuth.build_url
        warn(redirection)*/
        val redirection = TentAuth.redirect_url(hurl)
        error(redirection)
        openUri(redirection)




      }
    }

  }

 /* def respond(text: String) = {

    val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE).asInstanceOf[ConnectivityManager]
    val networkInfo = connMgr.getActiveNetworkInfo()
    if (networkInfo != null && networkInfo.isConnected) {

      error("ABout to request the codez~!")

      val code = HttpRequest.head(text).trustAllCerts().trustAllHosts().acceptEncoding(Creds.content).code()
      error("The code is" + code.toString)
      code


    }

    else {

      toast("No connection or BIG ERROR!")
      error("OH NOES!")
      "NO!!!!!"
    }
  }*/
}