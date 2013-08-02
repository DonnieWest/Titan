package com.donniewest.titan.tent

import android.content.Context


object Credentials {

//  stores credentials for after authentication

  var (accessToken, hawkKey, hawkAlgorithm, tokenType, clientID) = ("", "", "", "", "") //instantiate all my variables for this thing

  def getAccessToken(context: Context) = {

    val token = context.getSharedPreferences("TitanCredentials", Context.MODE_PRIVATE).getString("accessToken","")
    if (accessToken != "")
      accessToken
    else if (token != "")
      token
    else
      ""
  }
  def setAccessToken(token: String, context: Context) = {
    this.accessToken = token
    val tokenPersisted = context.getSharedPreferences("Titan_Credentials", Context.MODE_PRIVATE).edit()
    tokenPersisted.putString("accessToken",token)
    tokenPersisted.apply()
    true
  }

  def getHawkKey(context: Context) = {

    val key = context.getSharedPreferences("TitanCredentials", Context.MODE_PRIVATE).getString("hawkKey","")
    if (hawkKey != "")
      hawkKey
    else if (key != "")
      key
    else
      ""
  }

  def setHawkKey(key: String, context: Context) = {

    this.hawkKey = key
    val keyPersisted = context.getSharedPreferences("Titan_Credentials", Context.MODE_PRIVATE).edit()
    keyPersisted.putString("hawkKey",key)
    keyPersisted.apply()
    true

  }

  def getHawkAlgorithm(context: Context) = {
    val algorithm = context.getSharedPreferences("TitanCredentials", Context.MODE_PRIVATE).getString("hawkAlgorithm","")
    if (hawkAlgorithm != "")
      hawkAlgorithm
    else if (algorithm != "")
      algorithm
    else
      ""
  }
  def setHawkAlgorithm(algorithm: String, context: Context) = {


    this.hawkAlgorithm = algorithm
    val keyPersisted = context.getSharedPreferences("Titan_Credentials", Context.MODE_PRIVATE).edit()
    keyPersisted.putString("hawkAlgorithm",algorithm)
    keyPersisted.apply()
    true


  }

  def getTokenType(context:Context) = {

    val credTokenType = context.getSharedPreferences("TitanCredentials", Context.MODE_PRIVATE).getString("tokenType","")
    if (tokenType != "")
      tokenType
    else if (credTokenType != "")
      credTokenType
    else
      ""

  }
  def setTokenType(credTokenType: String, context: Context) = {
    this.tokenType = credTokenType
    val keyPersisted = context.getSharedPreferences("Titan_Credentials", Context.MODE_PRIVATE).edit()
    keyPersisted.putString("tokenType",credTokenType)
    keyPersisted.apply()
    true

  }

  def getClientID(context: Context) = {
    val ID = context.getSharedPreferences("TitanCredentials", Context.MODE_PRIVATE).getString("clientID","")
    if (clientID != "")
      clientID
    else if (ID != "")
      ID
    else
      ""

  }

  def setClientID(ID: String, context: Context) = {
    this.clientID = ID
    val keyPersisted = context.getSharedPreferences("Titan_Credentials", Context.MODE_PRIVATE).edit()
    keyPersisted.putString("clientID",ID)
    keyPersisted.apply()
    true

  }

  def deleteCredentials(context: Context) = {

    this.hawkKey = ""
    this.hawkAlgorithm = ""
    this.tokenType = ""
    this.clientID = ""          //first, blank variables
    context.getSharedPreferences("Titan_Credentials", Context.MODE_PRIVATE).edit().clear().commit()      //second, clear preferences

  }

}