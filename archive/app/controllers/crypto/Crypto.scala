package controllers.crypto

import java.security._
import java.security.spec.X509EncodedKeySpec
import java.io._
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

    val fis = new FileInputStream(getKeyFile(teacher))
    val encodedPrivateKey = new Array[Byte](fileKey.length().toInt)
    fis.read(encodedPrivateKey)
    fis.close()

    val keySpec = new X509EncodedKeySpec(encodedPrivateKey)
    keyFactory.generatePublic(keySpec)
  }

  def saveKey(teacher: String, key: String) = {
    if (!keysDir.exists()) {
      keysDir.mkdirs()
    }

    val fos = new FileOutputStream(getKeyFile(teacher))
    val keyBytes = getBytesFromString(key)
    fos.write(keyBytes)
    fos.close()
  }

  def hasKey(teacher: String) = {
    getKeyFile(teacher).exists()
  }

  private def getKeyFile(teacher: String) = {
    new File(keysDir + "/" + teacher + ".key")
  }

  def getBytesFromString(string: String): Array[Byte] = {
    string.toCharArray.map(_.toByte)
  }

  def getStringFromBytes(string: String): Array[Byte] = {
    string.toCharArray.map(_.toByte)
  }
}
