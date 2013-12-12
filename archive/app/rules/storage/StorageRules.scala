package rules.storage

import java.io.{FileInputStream, FileOutputStream, File}
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

  def saveGrades(courseId: Int, signature: Array[Byte], grades: Array[Byte]) = {
    if (!gradesDir.exists()) {
      gradesDir.mkdirs()
    }

    val fosg = new FileOutputStream(getGradesFile(courseId))
    fosg.write(grades)
    fosg.close()

    val foss = new FileOutputStream(getSignatureFile(courseId))
    foss.write(signature)
    foss.close()
  }

  def getDataFromFile(file: java.io.File): Array[Byte] = {
    val fis = new FileInputStream(file)
    val xmlBytes = new Array[Byte](file.length().toInt)
    fis.read(xmlBytes)
    fis.close()
    xmlBytes
  }
}
