package UI

import tent.Client
import com.actionbarsherlock.app.SherlockActivity
import com.donniewest.titan.R
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

        val entity = find[TextView](R.id.et_password).getText.toString
        Client.authenticate_with_server(entity)
//        openUri(redirection)
// The above ^ will be relevant when Tent 0.3 is released and the user has to go through the Oauth Flow. For now, nah
        error("Do I get this far?")
        Client.retrieve_feed(getApplicationContext)
        startActivity[PostFeedActivity]


      }
    }

  }

}