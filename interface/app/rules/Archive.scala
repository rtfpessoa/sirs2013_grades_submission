package rules

import play.api.libs.ws.WS
import play.api.libs.json.Json
import util.Crypto
import model.{UserSecretsTable, UserTable}
import model.traits.SecureStringFactory
import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class StudentGrade(name: String, username: String, grade: Int) {
  override def toString =
    s"""<student name="$name" username="$username" grade="$grade" />"""
}

case class CourseGrades(courseId: Long, courseName: String, teacherName: String, teacherUsername: String, grades: Seq[StudentGrade]) {
  override def toString =
    s"""<xml>
      |  <course id="$courseId" name="$courseName" />
      |  <teacher name="$teacherName" username="$teacherUsername" />
      |  <grades>
      |    ${grades.mkString}
      |  </grades>
      |</xml>
    """.stripMargin
}

object Archive {

  private val URL = "http://localhost:9001/storage/receive"

  def sendGrades(grades: CourseGrades) = {
    val user = UserTable.getByUsername(grades.teacherUsername).get
    val teacherPrivateKey = UserSecretsTable.getByUserId(user.id).get.privateKey
    val keyBytes = SecureStringFactory.fromSecureString(teacherPrivateKey.get)
    val xml = scala.xml.XML.loadString(grades.toString())
    val signature = Crypto.sign(Crypto.decodePrivateKey(keyBytes), Crypto.getBytesFromString(xml.toString()))

    val postData = Json.obj(
      "xml" -> grades.toString,
      "signature" -> Crypto.getStringFromBytes(signature)
    )

    val cipheredPostData = Json.obj(
      "payload" -> Crypto.encryptAES(postData.toString())
    )

    val responsePromise = WS.url(URL).post(cipheredPostData)
    Await.result(responsePromise, Duration(5, "seconds")).body
  }

}
