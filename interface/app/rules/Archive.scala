package rules

import play.api.libs.ws.WS
import play.api.libs.json.Json
import util.Crypto
import model.TeacherTable
import model.traits.SecureStringFactory
import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class StudentGrade(username: String, grade: Int) {
  override def toString =
    s"""<student name="$username" grade="$grade" />"""
}

case class ClassGrades(className: String, teacherUsername: String, grades: Seq[StudentGrade]) {
  override def toString =
    s"""<xml>
      |  <class name="$className" />
      |  <teacher username="$teacherUsername" />
      |  <grades>
      |    ${grades.mkString}
      |  </grades>
      |</xml>
    """.stripMargin
}

object Archive {

  private val URL = "http://localhost:9001/storage/receive"

  def sendGrades(grades: ClassGrades) = {
    val teacherPrivateKey = TeacherTable.getByUsername(grades.teacherUsername).get.privateKey
    val keyBytes = SecureStringFactory.fromSecureString(teacherPrivateKey.get)
    val signature = Crypto.sign(Crypto.decodePrivateKey(keyBytes), grades.toString().getBytes)

    val postData = Json.obj(
      "xml" -> grades.toString,
      "signature" -> new String(signature)
    )

    val responsePromise = WS.url(URL).post(postData)
    Await.result(responsePromise, Duration(5, "seconds")).body
  }

}
