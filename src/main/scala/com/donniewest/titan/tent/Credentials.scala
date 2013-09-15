package com.donniewest.titan.tent

import android.content.Context


object Credentials {

//  stores credentials for after authentication

  var (accessToken, hawkKey, hawkAlgorithm, tokenType, clientID) = ("", "", "", "", "") //instantiate all my variables for this thing

  def getAccessToken = accessToken


  def setAccessToken(token: String) = {
    this.accessToken = token
    this
  }

  def getHawkKey = hawkKey
  
  
  def setHawkKey(key: String) = {

    this.hawkKey = key
    this

  }

  def getHawkAlgorithm = hawkAlgorithm

  def setHawkAlgorithm(algorithm: String) = {


    this.hawkAlgorithm = algorithm
    this

  }

  def getTokenType = tokenType

  def setTokenType(credTokenType: String) = {
    this.tokenType = credTokenType
    this
  }

  def getClientID = clientID

  def setClientID(ID: String) = {
    this.clientID = ID
    this
  }

  def deleteCredentials = {

    this.hawkKey = ""
    this.hawkAlgorithm = ""
    this.tokenType = ""
    this.clientID = ""
  }

}
