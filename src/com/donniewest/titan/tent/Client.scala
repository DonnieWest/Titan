package tent

import tent.{Post, Authentication}

object Client {

  def authenticate_with_server(entity: String)  {

    Authentication.login(entity)

  }

  def send_post(content: String) {

    Post.send_post(content)

  }

  def retrieve_feed = {

  Post.retrieve_feed()
  }
}
