package tent


object Credentials {

//  stores credentials for after authentication


  var accessToken = ""
  def getAccessToken = accessToken
  def setAccessToken(token: String) = this.accessToken = token

  var hawkKey = ""
  def getHawkKey = hawkKey
  def setHawkKey(key: String) = this.hawkKey = key

  var hawkAlgorithm = ""
  def getHawkAlgorithm = hawkAlgorithm
  def setHawkAlgorithm(algorithm: String) = this.hawkAlgorithm = algorithm

  var tokenType = ""
  def getTokenType = tokenType
  def setTokenType(tokentype: String) = this.tokenType = tokentype

  var clientID = ""
  def getClientID = clientID
  def setClientID(ID: String) = this.clientID = ID

  var isLoggedIn = if (hawkKey != "") true else false



}
