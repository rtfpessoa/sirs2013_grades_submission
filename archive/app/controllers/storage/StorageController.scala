package controllers.storage

import play.api.mvc._
import java.io.{PrintWriter, File}
import play.api.libs.json.Json
import controllers.crypto.Crypto
import play.api.libs.ws.WS
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import rules.StorageRules.StorageRules

object StorageController extends Controller {
  def index(clazz: String, teacher: String) = Action {
    val folder = new File(".")

    Ok(Json.obj("class" -> clazz, "teacher" -> teacher))
  }

  def receive = Action {
    request =>
      val json = request.body.asJson.get

      val xmlOption = (json \ "xml").asOpt[String]
      val signatureOption = (json \ "signature").asOpt[String]

      val action = (xmlOption, signatureOption) match {
        case (Some(xmlString), Some(signatureString)) =>
          val xml = scala.xml.XML.loadString(xmlString)
          val signature = signatureString.toCharArray.map(_.toByte)

          val teacher = (xml \ "teacher" \ "@username").toString()
          val course = (xml \ "course" \ "@name").toString()

          if (checkSignature(teacher, signature, xml.toString().getBytes)) {
            val gradesWriter = new PrintWriter(StorageRules.getSignatureFile(course, teacher))
            gradesWriter.write(xml.toString())
            gradesWriter.close()

            val signatureWriter = new PrintWriter(StorageRules.getGradesFile(course, teacher))
            signatureWriter.write(signatureString)
            signatureWriter.close()

            Some(Ok(Json.obj("success" -> "")))
          } else {
            None
          }
        case _ => None
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

  def checkSignature(teacher: String, xml: Array[Byte], signature: Array[Byte]) = {
    val teacherKey = getTeacherKey(teacher)

    Crypto.verify(teacherKey, signature, xml)
  }
}
