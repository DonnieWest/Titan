package DB

import com.orm.SugarRecord

class Posts(app_name: String = "", app_url: String = "", app_id: String = "", content: String, entity: String, id: String, published: String, `type`: String) extends SugarRecord {

  val application_name = app_name
  val application_url = app_url
  val application_id = app_id
  val text = content
  val posting_entity = entity
  val post_id = id
  val published_at = published
  val post_type = `type`

}
