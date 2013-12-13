package rules.storage

import java.io.{FileInputStream, FileOutputStream, File}
import play.api.Play
import play.api.Play.current
import org.joda.time.DateTime

object StorageRules {
  val currentDir = new File("").getAbsolutePath

  val gradesDir = new File(currentDir + "/" + Play.configuration.getString("grades.dir").get)

  val backupDir = new File(currentDir + "/" + Play.configuration.getString("backup.dir").get)

  def getGradesFile(course: String) = {
    new File(gradesDir.getAbsolutePath + "/" + course + "-grades.xml")
  }

  def getSignatureFile(course: String) = {
    new File(gradesDir.getAbsolutePath + "/" + course + "-grades-signature.sig")
  }

  def getBackupFile(course: String) = {
    val timestamp = DateTime.now.getMillis.toString
    new File(backupDir.getAbsolutePath + "/" + course + "-" + timestamp + "-grades.bak")
  }

  def saveGrades(courseAbbrev: String, signature: Array[Byte], grades: Array[Byte]) = {
    if (!gradesDir.exists()) {
      gradesDir.mkdirs()
    }

    if (!backupDir.exists()) {
      backupDir.mkdirs()
    }

    val fosg = new FileOutputStream(getGradesFile(courseAbbrev))
    fosg.write(grades)
    fosg.close()

    val foss = new FileOutputStream(getSignatureFile(courseAbbrev))
    foss.write(signature)
    foss.close()

    val fosb = new FileOutputStream(getBackupFile(courseAbbrev))
    fosb.write(grades ++ signature)
    fosb.close()
  }

  def getDataFromFile(file: java.io.File): Array[Byte] = {
    val fis = new FileInputStream(file)
    val xmlBytes = new Array[Byte](file.length().toInt)
    fis.read(xmlBytes)
    fis.close()
    xmlBytes
  }
}
