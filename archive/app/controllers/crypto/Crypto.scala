package controllers.crypto

import java.security._
import java.security.spec.X509EncodedKeySpec
import java.io.{PrintWriter, FileInputStream, File}
import play.api.Play
import play.api.Play.current

object Crypto {
  def verify(key: PublicKey, signature: Array[Byte], data: Array[Byte]): Boolean = {
    val verifier = Signature.getInstance("SHA1withRSA")
    verifier.initVerify(key)
    verifier.update(data)
    verifier.verify(signature)
  }

  private val currentDir = new File("").getAbsolutePath
  private val keyFactory = KeyFactory.getInstance("RSA")
  private val keysDir = new File(currentDir + "/" + Play.configuration.getString("keys.dir").get)

  def loadKey(teacher: String): PublicKey = {
    val fileKey = getKeyFile(teacher)

    val fis = new FileInputStream(keysDir + "/" + teacher + ".key")
    val encodedKey = new Array[Byte](fileKey.length.toInt)
    fis.read(encodedKey)
    fis.close()

    val keySpec = new X509EncodedKeySpec(encodedKey)
    keyFactory.generatePublic(keySpec)
  }

  def saveKey(teacher: String, key: String) = {
    val keyWriter = new PrintWriter(getKeyFile(teacher))
    keyWriter.write(key)
    keyWriter.close()
  }

  def hasKey(teacher: String) = {
    getKeyFile(teacher).exists()
  }

  private def getKeyFile(teacher: String) = {
    new File(keysDir + "/" + teacher + ".key")
  }
}
