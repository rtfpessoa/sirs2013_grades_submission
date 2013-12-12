package controllers.crypto

import play.api.mvc._
import play.api.libs.json.Json
import rules.crypto.Crypto

object CryptoController extends Controller {

  def updatekey = Action {
    request =>
      val json = request.body.asJson.get

      val cipheredKey = (json \ "key").asOpt[String]

      val action = cipheredKey match {
        case Some(stringKey) =>

          val decipheredKey = Crypto.decryptRSA(stringKey)
          Crypto.update(decipheredKey)

          Some(Ok(Json.obj("success" -> "")))
        case _ =>
          None
      }

      action.getOrElse(Ok(Json.obj("error" -> "")))
  }

  def challenge = Action {
    request =>
      val uuid = java.util.UUID.randomUUID.toString.filter(_ != '-')

      Crypto.addChallenge(uuid)

      Ok(Json.obj("success" -> uuid))
  }
}
