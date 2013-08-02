package com.donniewest.titan

import com.donniewest.titan.tent.Client
import com.donniewest.titan.R
import android.widget.{TextView, Button}
import org.scaloid.common._
import android.os.Bundle
import scala.concurrent.ops._
import android.support.v7.app.ActionBarActivity


class LoginActivity extends ActionBarActivity with SActivity {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(
      new SVerticalLayout{

          val username = SEditText() inputType TEXT_URI
          SButton("Sign In", spawn{

            val entity = username.text.toString
            Client.authenticateServer(entity, getApplicationContext)
            /*openUri(redirection)
            This will be relevant when Tent 0.3 is released and the user has to go through the Oauth Flow*/
            Client.retrieveFeed(getApplicationContext)
            startActivity[PostFeedActivity]


          })
      }

    )

    }

  }