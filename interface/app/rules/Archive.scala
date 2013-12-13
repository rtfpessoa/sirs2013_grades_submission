package rules

import play.api.libs.ws.WS
import play.api.libs.json.Json
import util.Crypto
import model.{UserSecretsTable, UserTable}
import model.traits.SecureStringFactory
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.Play
import play.api.Play.current

case class StudentGrade(name: String, username: String, grade: Int) {
  override def toString =
    s"""<student name="$name" username="$username" grade="$grade" />"""
}

case class CourseGrades(courseId: Long, courseName: String, courseAbbrev: String, teacherName: String, teacherUsername: String, grades: Seq[StudentGrade]) {
  override def toString =
    s"""<xml>
      |  <course id="$courseId" name="$courseName" abbrev="$courseAbbrev" />
      |  <teacher name="$teacherName" username="$teacherUsername" />
      |  <grades>
      |    ${grades.mkString}
      |  </grades>
      |</xml>
    """.stripMargin
}

object Archive {

  private val URL_SUBMIT_GRADES = Play.configuration.getString("archive.url").get + "storage/receive"
  private val URL_REQUEST_CHALLENGE = Play.configuration.getString("archive.url").get + "challenge"

  def sendGrades(grades: CourseGrades) = {

    val user = UserTable.getByUsername(grades.teacherUsername).get
    val teacherPrivateKey = UserSecretsTable.getByUserId(user.id).get.privateKey
    val keyBytes = SecureStringFactory.fromSecureString(teacherPrivateKey.get)
    val xml = scala.xml.XML.loadString(grades.toString())
    val signature = Crypto.sign(Crypto.decodePrivateKey(keyBytes), Crypto.getBytesFromString(xml.toString()))

    val challengePromise = WS.url(URL_REQUEST_CHALLENGE).get()
    val requestBody = Await.result(challengePromise, Duration(5, "seconds")).body
    val json = Json.parse(requestBody)
    val challenge = (json \ "success").asOpt[String].get

    val postData = Json.obj(
      "challenge" -> challenge,
      "xml" -> grades.toString,
      "signature" -> Crypto.getStringFromBytes(signature)
    )

    val cipheredPostData = Json.obj(
      "payload" -> Crypto.encryptAES(postData.toString())
    )

    val responsePromise = WS.url(URL_SUBMIT_GRADES).post(cipheredPostData)
    Await.result(responsePromise, Duration(5, "seconds")).body
  }

}
