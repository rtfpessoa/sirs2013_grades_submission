package rules.StorageRules

import java.io.{FileOutputStream, File}
import play.api.Play
import play.api.Play.current

object StorageRules {
  val currentDir = new File("").getAbsolutePath

  val gradesDir = new File(currentDir + "/" + Play.configuration.getString("grades.dir").get)

  def getGradesFile(teacher: String, course: Int) = {
    new File(gradesDir.getAbsolutePath + "/" + course + "-" + teacher + "-grades.txt")
  }

  def getSignatureFile(teacher: String, course: Int) = {
    new File(gradesDir.getAbsolutePath + "/" + course + "-" + teacher + "-grades-signature.txt")
  }

  def saveGrades(teacherUsername: String, courseId: Int, grades: Array[Byte], signature: Array[Byte]) = {
    if (!gradesDir.exists()) {
      gradesDir.mkdirs()
    }

    val fosg = new FileOutputStream(getGradesFile(teacherUsername, courseId))
    fosg.write(grades)
    fosg.close()

    val foss = new FileOutputStream(getGradesFile(teacherUsername, courseId))
    foss.write(signature)
    foss.close()
  }
}
