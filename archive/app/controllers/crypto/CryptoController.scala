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

      (cipheredKey, signedKey) match {
        case (Some(stringKey), Some(stringSignature)) =>

          if (Crypto.verify(Crypto.loadKeyInterfacePubKey(), Crypto.getBytesFromString(stringSignature),
            Crypto.getBytesFromString(stringKey))) {

            val decipheredKey = Crypto.decryptRSA(stringKey)
            Crypto.update(decipheredKey)

            val answer = Json.obj("success" -> "The key was successfully updated!")
            val cipheredResponse = Crypto.encryptAES(answer.toString())

            Ok(Json.obj("payload" -> cipheredResponse))
          } else {
            Ok(Json.obj("error" -> "Signature did not match!"))
          }
        case _ => Ok(Json.obj("error" -> "Missing parameters!"))
      }
  }

  def challenge = Action {
    request =>
      val uuid = java.util.UUID.randomUUID.toString.filter(_ != '-')

      Crypto.addChallenge(uuid)

      Ok(Json.obj("success" -> uuid))
  }
}
