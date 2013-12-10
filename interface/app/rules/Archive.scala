package rules

import play.api.libs.ws.WS
import play.api.libs.json.Json
import util.Crypto
import model.TeacherTable
import model.traits.SecureStringFactory

case class StudentGrade(username: String, name: String, grade: Long) {
  def toXML =
    <studentGrade>
      <username>
        {username}
      </username>
      <name>
        {name}
      </name>
      <grade>
        {grade}
      </grade>
    </studentGrade>
}

case class ClassGrades(className: String, teacherUsername: String, grades: Seq[StudentGrade]) {
  def toXML =
    <classGrades>
      <className>
        {className}
      </className>
      <teacherUsername>
        {teacherUsername}
      </teacherUsername>
      <grades>
        {grades.map(_.toXML)}
      </grades>
    </classGrades>
}

object Archive {

  private val URL = "http://localhost:9001/storage/receive"

  def sendGrades(grades: ClassGrades) = {
    val teacherPrivateKey = TeacherTable.getByUsername(grades.teacherUsername).get.privateKey

    val signature = Crypto.sign(Crypto.decodePrivateKey(SecureStringFactory.fromSecureString(teacherPrivateKey).getBytes), grades.toXML.toString().getBytes)

    val postData = Json.obj(
      //"xml" -> grades.toXML,
      "signature" -> "value2"
    )

    WS.url(URL).post(postData)
  }

}
