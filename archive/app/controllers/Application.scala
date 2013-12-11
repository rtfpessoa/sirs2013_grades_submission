package controllers

import play.api.mvc._
import play.api.libs.json.Json
import java.security.{KeyFactory, KeyPairGenerator}
import java.security.spec.PKCS8EncodedKeySpec
import play.api.data.Form
import play.api.data.Forms._

object Application extends Controller {

  def index = Action {
    val classes = Seq("")
    Ok(views.html.index(classes))
  }

  case class GradesViewModel(student: String, grade: Long)

  def grades = Action {
    implicit request =>
      classForm.bindFromRequest.fold(
        formWithErrors => BadRequest(controllers.routes.Application.index().url),
        classId => {
          // TODO: Get grades from Archive

          Ok(views.html.grades(""))
        }
      )
  }

  private val classForm = Form(
    single(
      "classId" -> number
    ))

  def test = Action {
    val keyGen = KeyPairGenerator.getInstance("RSA")
    keyGen.initialize(1024)
    val generatedKeyPair = keyGen.genKeyPair()

    val originalBytes = generatedKeyPair.getPrivate.getEncoded
    val string = new String(originalBytes.map(_.toChar))

    val newBytes = string.toCharArray.map(_.toByte)

    val keyFactory = KeyFactory.getInstance("RSA")
    val privateKeySpecs = new PKCS8EncodedKeySpec(newBytes)
    val privateKey = keyFactory.generatePrivate(privateKeySpecs)

    haveDifferentElements(generatedKeyPair.getPrivate.getEncoded, privateKey.getEncoded).map {
      res =>
        Ok(Json.obj("errors" -> res))
    }.getOrElse(Ok(Json.obj("success" -> "")))
  }

  def haveDifferentElements(b1: Array[Byte], b2: Array[Byte]) =
    (b1, b2).zipped.map(_ == _)
      .zipWithIndex.find(!_._1)
      .map {
      case (_, i) => "b1(%d) != b2(%d)" format(i, i)
    }
}