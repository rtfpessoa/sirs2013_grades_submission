import model._
import model.traits.SecureString
import play.api._
import play.api.libs.json.Json
import play.api.libs.ws.WS
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import util.Crypto
import play.api.Play
import play.api.Play.current

object Global extends GlobalSettings {

  val RETRIES = 5

  implicit def toSecureString(str: String): SecureString = SecureString(str)

  implicit def fromSecureString(str: SecureString): String = str.toString

  override def onStart(app: Application) {
    Logger.info("Application has started")

    PopulateData.populate()
    exchangeComunicationKey()

    UserTable.getAll.filter(_.level != UserLevel.Student).map {
      user =>
        val secrets = UserSecretsTable.getByUserId(user.id).get
        if (secrets.privateKey.isEmpty) {
          val (publicKey, privateKey) = Crypto.generateKeyPair()
          UserSecretsTable.update(UserSecrets(secrets.id, secrets.userId, secrets.password,
            Some(publicKey), Some(privateKey)))
        }
    }

  }

  def exchangeComunicationKey(): Unit = {
    val URL = Play.configuration.getString("archive.url").get + "updatekey"
    val newKeyString = Crypto.generateSymetricKey()

    val cipheredKey = Crypto.encryptRSA(newKeyString)
    val signedKey = Crypto.sign(Crypto.loadKeyInterfacePvtKey(), Crypto.getBytesFromString(cipheredKey))

    val postData = Json.obj(
      "key" -> cipheredKey,
      "signature" -> Crypto.getStringFromBytes(signedKey)
    )

    import scala.util.control.Breaks._
    breakable {
      for (i <- 0 to RETRIES) {
        try {
          val responsePromise = WS.url(URL).post(postData)
          val response = Await.result(responsePromise, Duration(5, "seconds")).body
          val json = Json.parse(response)

          if ((json \ "success").asOpt[String].isDefined) {
            Crypto.update(newKeyString)
            println("The communication key was exchanged with success!")
            break()
          } else {
            println((json \ "error").as[String])
          }
        } catch {
          case t: Exception => println("Key exchange failed!")
        }
      }

      println("Ups: there was a problem establishing the communication key!")
    }
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

}
