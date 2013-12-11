package rules.StorageRules

import java.io.File
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
}
