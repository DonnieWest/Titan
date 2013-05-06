package com.donniewest.titan

import scala.beans.BeanProperty


object Credentials {

  @BeanProperty var hawk_key = ""
  @BeanProperty var hawk_algorithm = ""
  @BeanProperty var client_id = ""
  @BeanProperty var hawk_id = ""


}
