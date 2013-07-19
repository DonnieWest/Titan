package tent

object identityJson {

//  contains Json strings for authentication


  val registration = """{
  "type": "https://tent.io/types/app/v0#",
  "content": {
    "name": "Titan",
    "url": "https://tent.donniewest.com",
    "types": {
    "read": [
    "https://tent.io/types/app/v0"
    ],
    "write": [
    "https://tent.io/types/status/v0",
    "https://tent.io/types/photo/v0"
    ]
  },
    "redirect_uri": "titan://oauth"
  },
  "permissions": {
    "public": false
  }
}"""

}