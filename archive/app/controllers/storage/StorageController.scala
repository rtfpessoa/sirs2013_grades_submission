package controllers.storage

import play.api.mvc._
import java.io.{PrintWriter, File}
import play.api.libs.json.Json
import controllers.crypto.Crypto
import play.api.Play
import play.api.Play.current

object StorageController extends Controller {

  private val currentDir = new File(".").getAbsolutePath

  def index(clazz: String, teacher: String) = Action {
    val folder = new File(".")

    Ok(Json.obj("class" -> clazz, "teacher" -> teacher))
  }

  def receive = Action {
    request =>
      val payload = request.getQueryString("payload")

      request.queryString.map(println)

      val action = payload.map {
        message =>
          val messageJson = Json.parse(message)

          val xmlString = (messageJson \ "xml").as[String]
          val xml = scala.xml.XML.loadString(xmlString)

          val signature = (messageJson \ "signature").as[String]

          val teacher = (xml \ "teacher" \ "@name").toString()
          val clazz = (xml \ "class" \ "@name").toString()
          println("START")
          if (checkSignature(teacher, signature.getBytes, xmlString.getBytes)) {
            println("STORING")
            val gradesWriter = new PrintWriter(getSignatureFile(clazz, teacher))
            gradesWriter.write(xml.toString())
            gradesWriter.close()

            val signatureWriter = new PrintWriter(getGradesFile(clazz, teacher))
            signatureWriter.write(signature)
            signatureWriter.close()

            Some(Ok(Json.obj("success" -> "")))
          } else {
            println("FAILING")
            None
          }
      }.getOrElse(None)
      println("HERE")
      action.getOrElse(Ok(Json.obj("error" -> "")))
  }

  def getTeacherKey(teacher: String) = {
    Crypto.loadKey(teacher)
    /*if (Crypto.hasKey(teacher)) {
      Crypto.loadKey(teacher)
    } else {
      val key = WS.url("http://localhost:9000/security/key/" + teacher).get()
      Crypto.saveKey(teacher, key)
    }*/
  }

  def checkSignature(teacher: String, xml: Array[Byte], signature: Array[Byte]) = {
    val teacherKey = getTeacherKey(teacher)

    Crypto.verify(teacherKey, signature, xml)
  }

  private val gradesDir = new File(currentDir + "/" + Play.configuration.getString("grades.dir").get).getAbsolutePath

  private def getGradesFile(clazz: String, teacher: String) = {
    new File(gradesDir + "/" + clazz + "-" + teacher + "-grades.txt")
  }

  private def getSignatureFile(clazz: String, teacher: String) = {
    new File(gradesDir + "/" + clazz + "-" + teacher + "-grades-signature.txt")
  }
}
