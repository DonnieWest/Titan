package tent

import scala.beans.BeanProperty


object Temp_Credentials {

  @BeanProperty var hawk_key = ""
  @BeanProperty var hawk_algorithm = ""
  @BeanProperty var client_id = ""
  @BeanProperty var hawk_id = ""


}
