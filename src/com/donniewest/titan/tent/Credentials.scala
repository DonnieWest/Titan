package tent

import scala.beans.BeanProperty


object Credentials {

  @BeanProperty var access_token = ""
  @BeanProperty var hawk_key = ""
  @BeanProperty var hawk_algorithm = ""
  @BeanProperty var token_type = ""
// note to self, Client_ID must also be stored in the database with the above credentials.
}