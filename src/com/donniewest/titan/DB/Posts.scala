package DB

import com.orm.SugarRecord
import android.content.Context

class Posts(context: Context,
  appName: String = "",
  appUrl: String = "",
  appID: String = "",
  content: String,
  entity: String,
  id: String,
  published: String,
  `type`: String) extends SugarRecord(context: Context) {



  val applicationName = appName
  val applicationURL = appUrl
  val applicationID = appID
  val text = content
  val postingEntity = entity
  val postID = id
  val publishedAt = published
  val postType = `type`

}
