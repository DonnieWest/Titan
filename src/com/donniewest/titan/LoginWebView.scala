import android.os.Bundle
import org.scaloid.common._


class LoginWebView extends SActivity {
    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      setContentView{

        new SVerticalLayout{

          SWebView()

        }


      }
    }
}