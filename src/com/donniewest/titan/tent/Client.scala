package tent

import tent.{Post, Authentication}
import android.content.Context

object Client {

  def authenticate_with_server(entity: String)  {

    Authentication.login(entity)

  }

  def send_post(content: String) {

    Post.send_post(content)

  }

  def retrieve_feed(context: Context) = {

  Post.retrieve_feed(context)
  }
}
