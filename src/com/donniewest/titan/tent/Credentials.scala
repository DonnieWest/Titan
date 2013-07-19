package tent

import scala.beans.BeanProperty


object Credentials {

  @BeanProperty var accessToken = ""
  @BeanProperty var hawkKey = ""
  @BeanProperty var hawkAlgorithm = ""
  @BeanProperty var tokenType = ""
  @BeanProperty var clientID = ""
}
