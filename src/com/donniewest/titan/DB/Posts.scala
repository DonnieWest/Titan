package DB

import com.orm.SugarRecord
import android.content.Context

class Posts(context: Context,
  app_name: String = "",
  app_url: String = "",
  app_id: String = "",
  content: String,
  entity: String,
  id: String,
  published: String,
  `type`: String) extends SugarRecord(context: Context) {



  val applicationName = app_name
  val applicationURL = app_url
  val applicationID = app_id
  val text = content
  val postingEntity = entity
  val postID = id
  val publishedAt = published
  val postType = `type`

}
