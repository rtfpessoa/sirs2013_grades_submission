package controllers.crypto

import play.api.mvc._
import play.api.libs.json.Json
import rules.crypto.Crypto

object CryptoController extends Controller {

  def updatekey = Action {
    request =>
      val json = request.body.asJson.get

      val cipheredKey = (json \ "key").asOpt[String]
      val signedKey = (json \ "signature").asOpt[String]

      val action = (cipheredKey, signedKey) match {
        case (Some(stringKey), Some(stringSignature)) =>

          if (Crypto.verify(Crypto.loadKeyInterfacePubKey(), Crypto.getBytesFromString(stringSignature),
            Crypto.getBytesFromString(stringKey))) {
            val decipheredKey = Crypto.decryptRSA(stringKey)
            Crypto.update(decipheredKey)

            Json.obj("success" -> "The key was successfull updated!")
          } else {
            Json.obj("error" -> "Signature did not match!")
          }
        case _ =>
          Json.obj("error" -> "Missing parameters!")
      }

      Ok(action)
  }

  def challenge = Action {
    request =>
      val uuid = java.util.UUID.randomUUID.toString.filter(_ != '-')

      Crypto.addChallenge(uuid)

      Ok(Json.obj("success" -> uuid))
  }
}
