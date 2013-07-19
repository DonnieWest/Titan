package tent

import scala.beans.BeanProperty


object TempCredentials {

//  Stores the temporary credentials retrieved during the Oauth portion of Tent's Hawk Authentication

  @BeanProperty var hawkKey = ""
  @BeanProperty var hawkAlgorithm = ""
  @BeanProperty var hawkID = ""


}
