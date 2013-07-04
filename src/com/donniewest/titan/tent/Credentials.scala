package tent

import scala.beans.BeanProperty


object Credentials {

  @BeanProperty var access_token = ""
  @BeanProperty var hawk_key = ""
  @BeanProperty var hawk_algorithm = ""
  @BeanProperty var token_type = ""
  @BeanProperty var client_id = ""
}
