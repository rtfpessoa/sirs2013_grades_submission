package rules.StorageRules

import java.io.{FileOutputStream, File}
import play.api.Play
import play.api.Play.current

object StorageRules {
  val currentDir = new File("").getAbsolutePath

  val gradesDir = new File(currentDir + "/" + Play.configuration.getString("grades.dir").get)

  def getGradesFile(course: Int) = {
    new File(gradesDir.getAbsolutePath + "/" + course + "-grades.txt")
  }

  def getSignatureFile(course: Int) = {
    new File(gradesDir.getAbsolutePath + "/" + course + "-grades-signature.txt")
  }

  def saveGrades(courseId: Int, grades: Array[Byte], signature: Array[Byte]) = {
    if (!gradesDir.exists()) {
      gradesDir.mkdirs()
    }

    val fosg = new FileOutputStream(getGradesFile(courseId))
    fosg.write(grades)
    fosg.close()

    val foss = new FileOutputStream(getGradesFile(courseId))
    foss.write(signature)
    foss.close()
  }
}
