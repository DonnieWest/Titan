package UI

import android.os.Bundle
import com.orm.query.Select
import DB.Posts
import org.scaloid.common._

class PostFeedActivity extends SActivity{

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    val posts = Select.from(classOf[Posts]).orderBy("Published").list().toArray
    val adapter = new SArrayAdapter(posts)

    setContentView(

      new SVerticalLayout{

        SListView().setAdapter(adapter)

      }

    )


    }
}
