import com.donniewest.titan.Creds
import net.liftweb.json._



class Interact {



  def get_posts = {


    val uri = "/posts?" + (if (User.getLast_post_id == 0) "limit=10" else "since_id=" + User.getLast_post_id) + "?posts_type=https://tent.io/types/post/status/v0.1.0"
    val response_body = Creds.get_signed_body(uri)
    case class Post(entity: String, licenses: String, content: String, published_at: String, id: String, attachments: String, mentions: Array[String])
    val json = parse(response_body)
    /*json.extract[Post]*/

  }
/*
{"entity":"https://donniewest.tent.is","licenses":[],"content":{"text":"^arturovm ^https://jcook.cc Well, off I go to check this out. Reporting back soon, thanks guys!"},"published_at":1359263868,"permissions":{"public":true},"id":"gPCUSQcjFtAdNEWCHi1YGQ","attachments":[],"type":"https://tent.io/types/post/status/v0.1.0","version":1,"app":{"name":"TentStatus","url":"https://apps.tent.is/status"},"mentions":[{"entity":"https://arturovm.tent.is","post":"rpx5Cb4HZRaX0WkAxMtyTA"},{"entity":"https://jcook.cc"}]}
**/

  def get_profile = {


    val uri = "/profile"
    val response_body = Creds.get_signed_body(uri)
    case class Profile(name: String, avatar_url: String, location: String, bio: String)
    val json = parse(response_body)
/*
    json.extract[Profile]
*/


  }


  def create_post(content: String) = {

    val uri = "/posts"
    val post_json = """{
  "type": "https://tent.io/types/post/status/v0.1.0",
  "published_at":""" + Creds.getCurrent_timestamp + """,
  "permissions": {
    "public": true
  },
  "licenses": [
    "http://creativecommons.org/licenses/by/3.0/"
  ],
  "content": {
    "text": """" + content + """",
    }
  }
}"""
    Creds.send_signed_json(uri, post_json)

  }

}

/*

{
  "https://tent.io/types/info/basic/v0.1.0": {
    "name": "The Tentity",
    "avatar_url": "http://example.org/avatar.jpg",
    "birthdate": "2012-08-23",
    "location": "The Internet",
    "gender": "Unknown",
    "bio": "Asperiores ad qui maxime eum quia aliquid omnis mollitia.",
    "permissions": {
      "public": true
    },
    "version": 1
  },
  "https://tent.io/types/info/core/v0.1.0": {
    "licenses": [
      "http://creativecommons.org/licenses/by/3.0/"
    ],
    "entity": "https://example.org",
    "servers": [
      "https://tent.example.com",
      "http://eqt5g4fuenphqinx.onion/"
    ],
    "permissions": {
      "public": true
    },
    "version": 1,
    "tent_version": "0.2"
  }
}

**/