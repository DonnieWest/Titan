package tent

import tent.{Post, Authentication}
import android.content.Context

object Client {

  def authenticateServer(entity: String)  {

    Authentication.login(entity)

  }

  def sendPost(content: String) {

    Post.send_post(content)

  }

  def retrieveFeed(context: Context) = {

  Post.retrieve_feed(context)
  }
}
