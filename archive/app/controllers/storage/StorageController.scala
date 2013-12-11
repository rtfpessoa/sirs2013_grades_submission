package controllers.storage

import play.api.mvc._
import java.io.File
import play.api.libs.json.Json
import controllers.crypto.Crypto
import play.api.libs.ws.WS
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import rules.StorageRules.StorageRules

object StorageController extends Controller {
  def index(course: Int, teacher: String) = Action {
    val folder = new File(".")

    Ok(Json.obj("class" -> course, "teacher" -> teacher))
  }

  def receive = Action {
    request =>
      val json = request.body.asJson.get

      val xmlOption = (json \ "xml").asOpt[String]
      val signatureOption = (json \ "signature").asOpt[String]

      val action = (xmlOption, signatureOption) match {
        case (Some(xmlString), Some(signatureString)) =>
          val xml = scala.xml.XML.loadString(xmlString)
          val signatureBytes = Crypto.getBytesFromString(signatureString)

          val teacherUsername = (xml \ "teacher" \ "@username").toString()
          val courseId = (xml \ "course" \ "@id").toString().toInt

          val gradesBytes = Crypto.getBytesFromString(xml.toString())
          if (checkSignature(teacherUsername, signatureBytes, gradesBytes)) {
            StorageRules.saveGrades(teacherUsername, courseId, signatureBytes, gradesBytes)

            Some(Ok(Json.obj("success" -> "")))
          }
          else {
            None
          }
        case _ =>
          None
      }

      action.getOrElse(Ok(Json.obj("error" -> "")))
  }

  def getTeacherKey(teacher: String) = {
    if (!Crypto.hasKey(teacher)) {
      val responsePromise = WS.url("http://localhost:9000/security/key/" + teacher).get()
      val json = Await.result(responsePromise, Duration(5, "seconds")).json
      val keyOption = (json \ "key").asOpt[String]

      if (keyOption.isDefined) {
        Crypto.saveKey(teacher, keyOption.get)
      }
    }

    Crypto.loadKey(teacher)
  }

  def checkSignature(teacher: String, signature: Array[Byte], xml: Array[Byte]) = {
    val teacherKey = getTeacherKey(teacher)

    Crypto.verify(teacherKey, signature, xml)
  }
}
