import model._
import model.traits.SecureString
import play.api._
import play.api.libs.json.Json
import play.api.libs.ws.WS
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import util.Crypto

object Global extends GlobalSettings {

  val RETRIES = 10

  implicit def toSecureString(str: String): SecureString = SecureString(str)

  implicit def fromSecureString(str: SecureString): String = str.toString

  override def onStart(app: Application) {
    Logger.info("Application has started")

    UserTable.getAll.filter(_.level != UserLevel.Student).map {
      user =>
        val secrets = UserSecretsTable.getByUserId(user.id).get
        if (secrets.privateKey.isEmpty) {
          val (publicKey, privateKey) = Crypto.generateKeyPair()
          UserSecretsTable.update(UserSecrets(secrets.id, secrets.userId, secrets.password,
            Some(publicKey), Some(privateKey)))
        }

    }
    // Check rules.UserRules: it already has some methods!
    //    val (publicKey, privateKey) = Crypto.generateKeyPair()
    //    TeacherTable.create(TeacherFactory.apply("Rafael", "ist169801", MD5.hash("password"),
    //      Some(publicKey), Some(privateKey)))

    exchangeComunicationKey()
  }

  def exchangeComunicationKey(): Unit = {
    val URL = "http://localhost:9001/updatekey"
    val newKeyString = Crypto.generateSymetricKey()

    val cipheredKey = Crypto.encryptRSA(newKeyString)

    val postData = Json.obj(
      "key" -> cipheredKey
    )

    for (i <- 0 to RETRIES) {
      val responsePromise = WS.url(URL).post(postData)
      val response = Await.result(responsePromise, Duration(5, "seconds")).body
      val json = Json.parse(response)

      if ((json \ "success").asOpt[String].isDefined) {
        println("The communication key was exchanged with success!")
        return
      }
    }

    println("Ups: there was a problem establishing the communication key")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

}
