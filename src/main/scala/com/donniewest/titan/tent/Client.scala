package com.donniewest.titan.tent

import com.donniewest.titan.tent.{Post, Authentication}

object Client {

  def authenticateServer(entity: String)  {

//  Uses chosen entity to authenticate with server


    Authentication.one_leg_login(entity)

  }

  def sendPost(content: String) {

//  sends simple status post given set content

    Post.send_post(content)

  }

  def retrieveFeed = {

//  retrieves feed given application context


  Post.retrieve_feed
  }
}
