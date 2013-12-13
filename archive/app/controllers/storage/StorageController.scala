package controllers.storage

import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.ws.WS
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import rules.storage.StorageRules
import play.api.data._
import play.api.data.Forms._
import rules.crypto.Crypto
import java.security.PublicKey
import play.api.Play
import play.api.Play.current

object StorageController extends Controller {

  case class StudentViewModel(username: String, name: String, grade: Long)

  def index = Action {
    implicit request =>
      courseForm.bindFromRequest.fold(
        formWithErrors => BadRequest(controllers.routes.Application.index().url),
        courseAbbrev => {
          val gradesFile = StorageRules.getGradesFile(courseAbbrev)
          val gradesBytes = StorageRules.getDataFromFile(gradesFile)
          val gradesXml = scala.xml.XML.loadString(Crypto.getStringFromBytes(gradesBytes))

          val signatureFile = StorageRules.getSignatureFile(courseAbbrev)
          val signatureBytes = StorageRules.getDataFromFile(signatureFile)

          val courseName = (gradesXml \ "course" \ "@name").toString()
          val teacherName = (gradesXml \ "teacher" \ "@name").toString()
          val teacherUsername = (gradesXml \ "teacher" \ "@username").toString()

          if (checkSignature(teacherUsername, signatureBytes, gradesBytes)) {
            val grades = (gradesXml \\ "student").map {
              student =>
                val studentName = (student \ "@name").toString()
                val studentUsername = (student \ "@username").toString()
                val studentGrade = (student \ "@grade").toString().toInt
                StudentViewModel(studentUsername, studentName, studentGrade)
            }
            Ok(views.html.grades(courseName, teacherName, grades))
          }
          else {
            Ok(Json.obj("error" -> "The grades are corrupted!"))
          }
        }
      )
  }

  private val courseForm = Form(
    single(
      "courseAbbrev" -> text
    ))

  def receive = Action {
    request =>
      val data = request.body.asJson.get

      val payload = (data \ "payload").asOpt[String]
      payload.map {
        data =>
          val decipheredRequest = Crypto.decryptAES(data)

          val json = Json.parse(decipheredRequest)

          val challengeOption = (json \ "challenge").asOpt[String]
          if (challengeOption.isDefined &&
            Crypto.removeChallenge(challengeOption.get).isDefined) {

            val xmlOption = (json \ "xml").asOpt[String]
            val signatureOption = (json \ "signature").asOpt[String]

            (xmlOption, signatureOption) match {
              case (Some(xmlString), Some(signatureString)) =>
                val xml = scala.xml.XML.loadString(xmlString)
                val signatureBytes = Crypto.getBytesFromString(signatureString)

                val teacherUsername = (xml \ "teacher" \ "@username").toString()
                val courseAbbrev = (xml \ "course" \ "@abbrev").toString()

                val gradesBytes = Crypto.getBytesFromString(xml.toString())
                if (checkSignature(teacherUsername, signatureBytes, gradesBytes)) {
                  StorageRules.saveGrades(courseAbbrev, signatureBytes, gradesBytes)

                  Ok(Json.obj("success" -> "Grades submited with success!"))
                }
                else {
                  Ok(Json.obj("error" -> "Signature verification failed!"))
                }
              case _ => Ok(Json.obj("error" -> "Missing parameters!"))
            }

          } else {
            Ok(Json.obj("error" -> "Challenge verification failed!"))
          }
      }.getOrElse(Ok(Json.obj("error" -> "No payload found!")))
  }

  def getTeacherKey(teacher: String): Option[PublicKey] = {
    val URL = Play.configuration.getString("interface.url").get + "security/key/" + teacher
    val responsePromise = WS.url(URL).get()
    val json = Await.result(responsePromise, Duration(5, "seconds")).json

    val keyOption = (json \ "key").asOpt[String]
    val signatureOption = (json \ "signature").asOpt[String]

    if (keyOption.isDefined && signatureOption.isDefined) {
      val keyBytes = Crypto.getBytesFromString(keyOption.get)
      val signatureBytes = Crypto.getBytesFromString(signatureOption.get)

      if (Crypto.verify(Crypto.loadKeyInterfacePubKey(), signatureBytes, keyBytes)) {
        Some(Crypto.getPublicKeyFromBytes(keyBytes))
      } else {
        None
      }
    } else {
      None
    }
  }

  def checkSignature(teacher: String, signature: Array[Byte], xml: Array[Byte]) = {
    val teacherKey = getTeacherKey(teacher)
    teacherKey.isDefined && Crypto.verify(teacherKey.get, signature, xml)
  }
}
