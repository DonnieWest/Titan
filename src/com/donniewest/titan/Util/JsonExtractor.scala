package Util

import net.liftweb.json._
import net.liftweb.json.JsonAST.JValue

object JsonExtractor {

  def extract(from_json: JValue, to_extract: String ) = {

//    Utility method to extract simple values from Json

    compact(render(from_json \\ to_extract)).replace("\"","")



  }

}
