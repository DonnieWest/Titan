package tent

import tent.{Post, Authentication}
import android.content.Context

object Client {

  def authenticateServer(entity: String)  {

//  Uses chosen entity to authenticate with server


    Authentication.login(entity)

  }

  def sendPost(content: String) {

//  sends simple status post given set content

    Post.send_post(content)

  }

  def retrieveFeed(context: Context) = {

//  retrieves feed given application context


  Post.retrieve_feed(context)
  }
}
