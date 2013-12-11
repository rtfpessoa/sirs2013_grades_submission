import model.{Teacher, TeacherTable}
import play.api._
import play.api.libs.json.Json
import play.api.libs.ws.WS
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import util.Crypto
import model.traits.SecureStringFactory._

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

    exchangeComunicationKey()
  }

  def exchangeComunicationKey() = {
    val URL = "http://localhost:9001/updatekey"
    val newKeyString = Crypto.generateSymetricKey()

    val cipheredKey = Crypto.encryptRSA(newKeyString)

    val postData = Json.obj(
      "key" -> cipheredKey
    )

    val responsePromise = WS.url(URL).post(postData)
    val responseBody = Await.result(responsePromise, Duration(5, "seconds")).body
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

}