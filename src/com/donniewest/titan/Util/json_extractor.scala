package com.donniewest.titan.Util

import net.liftweb.json._
import net.liftweb.json.JsonAST.JValue

object json_extractor {

  def extract(from_json: JValue, to_extract: String ) = {


    compact(render(from_json \\ to_extract)).replace("\"","")



  }

}
