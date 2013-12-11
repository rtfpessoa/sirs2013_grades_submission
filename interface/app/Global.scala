import model.traits.{SecureString, SecureStringFactory}
import model.{TeacherFactory, Teacher, TeacherTable}
import play.api._
import util.Crypto
import util.MD5

implicit def toSecureString(str: String): SecureString = SecureString(str)
implicit def fromSecureString(str: SecureString): String = str.toString

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")

    TeacherTable.getAll.map {
      teacher =>
        if (teacher.privateKey.isEmpty) {
          val (publicKey, privateKey) = Crypto.generateKeyPair()
          TeacherTable.update(Teacher(teacher.id, teacher.name, teacher.username, teacher.password,
            Some(publicKey), Some(privateKey)))
        }

    }
//    val (publicKey, privateKey) = Crypto.generateKeyPair()
//    TeacherTable.create(TeacherFactory.apply("Rafael", "ist169801", MD5.hash("password"),
//      Some(publicKey), Some(privateKey)))
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

}