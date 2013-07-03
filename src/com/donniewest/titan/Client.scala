package com.donniewest.titan

import com.github.kevinsawicki.http.HttpRequest
import tent.{Post, Hawk_Headers, Endpoints, Authentication}
import net.liftweb.json._
import android.content.ContentValues
import scala.collection.mutable


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
