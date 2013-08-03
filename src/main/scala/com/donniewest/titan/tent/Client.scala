package com.donniewest.titan.tent

import com.donniewest.titan.tent.{Post, Authentication}
import android.content.Context

object Client {

  def authenticateServer(entity: String, context: Context)  {

//  Uses chosen entity to authenticate with server


    Authentication.login(context, entity)

  }

  def sendPost(content: String, context: Context) {

//  sends simple status post given set content

    Post.send_post(content, context)

  }

  def retrieveFeed(context: Context) = {

//  retrieves feed given application context


  Post.retrieve_feed(context)
  }
}
