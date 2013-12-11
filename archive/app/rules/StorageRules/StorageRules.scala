package rules.StorageRules

import java.io.File
import play.api.Play
import play.api.Play.current

object StorageRules {
  val currentDir = new File("").getAbsolutePath

  val gradesDir = new File(currentDir + "/" + Play.configuration.getString("grades.dir").get).getAbsolutePath

  def getGradesFile(clazz: String, teacher: String) = {
    new File(gradesDir + "/" + clazz + "-" + teacher + "-grades.txt")
  }

  def getSignatureFile(clazz: String, teacher: String) = {
    new File(gradesDir + "/" + clazz + "-" + teacher + "-grades-signature.txt")
  }
}
