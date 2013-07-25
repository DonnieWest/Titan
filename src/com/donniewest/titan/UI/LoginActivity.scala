package UI

import tent.Client
import com.donniewest.titan.R
import android.widget.{TextView, Button}
import org.scaloid.common._
import android.os.Bundle
import scala.concurrent.ops._


class LoginActivity extends SActivity {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(
      new SVerticalLayout{


          val pass = SEditText() inputType TEXT_PASSWORD
          SButton("Sign In").onClick{

            spawn{

            val entity = pass.text.toString
            Client.authenticateServer(entity, getApplicationContext)
            /*        openUri(redirection)
            This will be relevant when Tent 0.3 is released and the user has to go through the Oauth Flow*/
            Client.retrieveFeed(getApplicationContext)
            startActivity[PostFeedActivity]


          }}

      }

    )

    }

  }