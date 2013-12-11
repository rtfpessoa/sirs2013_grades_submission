import model.traits.SecureStringFactory
import model.{Teacher, TeacherFactory, TeacherTable}
import play.api._
import util.Crypto

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")

    TeacherTable.getAll.map {
      teacher =>
        if (teacher.privateKey.isEmpty) {
          val (privateKey, publicKey) = Crypto.generateKeyPair()
          TeacherTable.update(Teacher(teacher.id, teacher.name, teacher.username, teacher.password,
            Some(SecureStringFactory.toSecureString(publicKey)), Some(SecureStringFactory.toSecureString(privateKey))))
        }
    }
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

}