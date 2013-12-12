package rules.crypto

import java.security._
import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}
import java.io._
import javax.crypto.Cipher
import play.api.libs.{Crypto => PlayCrypto}
import scala.collection.mutable

object Crypto {

  private val currentDir = new File("").getAbsolutePath
  private var a = ""
  private val challenges = mutable.Map[String, Boolean]()

  def addChallenge(challenge: String) = {
    challenges.put(challenge, true)
  }

  def removeChallenge(challenge: String) = {
    challenges.remove(challenge)
  }

  def update(data: String) = {
    a = data
  }

  def decryptRSA(data: String): String = {
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.DECRYPT_MODE, loadKeyArchivePvtKey())

    val dataBytes = getBytesFromString(data)
    val decipheredData = cipher.doFinal(dataBytes)
    getStringFromBytes(decipheredData)
  }

  def decryptKeyWithRSA(data: String): String = {
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.DECRYPT_MODE, loadKeyInterfacePubKey())

    val dataBytes = getBytesFromString(data)
    val decipheredData = cipher.doFinal(dataBytes)
    getStringFromBytes(decipheredData)
  }

  def decryptAES(data: String): String = {
    PlayCrypto.decryptAES(data, a)
  }

  def verify(key: PublicKey, signature: Array[Byte], data: Array[Byte]): Boolean = {
    val verifier = Signature.getInstance("SHA1withRSA")
    verifier.initVerify(key)
    verifier.update(data)
    verifier.verify(signature)
  }

  def loadKeyInterfacePubKey(): PublicKey = {
    getPublicKeyFromBytes(loadKeyFromFile("ipublic.key"))
  }

  def loadKeyArchivePvtKey(): PrivateKey = {
    getPrivateKeyFromBytes(loadKeyFromFile("aprivate.key"))
  }

  def loadKeyFromFile(keyName: String): Array[Byte] = {
    val fileKey = new File(currentDir + "/conf/" + keyName)

    val fis = new FileInputStream(fileKey)
    val keyBytes = new Array[Byte](fileKey.length().toInt)
    fis.read(keyBytes)
    fis.close()

    keyBytes
  }

  def getPrivateKeyFromBytes(keyBytes: Array[Byte]): PrivateKey = {
    val keyFactory = KeyFactory.getInstance("RSA")
    val keySpec = new PKCS8EncodedKeySpec(keyBytes)
    keyFactory.generatePrivate(keySpec)
  }

  def getPublicKeyFromBytes(keyBytes: Array[Byte]): PublicKey = {
    val keyFactory = KeyFactory.getInstance("RSA")
    val keySpec = new X509EncodedKeySpec(keyBytes)
    keyFactory.generatePublic(keySpec)
  }

  def getBytesFromString(string: String): Array[Byte] = {
    string.toCharArray.map(_.toByte)
  }

  def getStringFromBytes(bytes: Array[Byte]): String = {
    new String(bytes.map(_.toChar))
  }

}
