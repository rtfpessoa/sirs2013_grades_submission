package util

import java.security._
import java.security.spec.{X509EncodedKeySpec, PKCS8EncodedKeySpec}
import java.io.{FileInputStream, File}
import javax.crypto.Cipher
import play.api.libs.{Crypto => PlayCrypto}

object Crypto {

  private val currentDir = new File("").getAbsolutePath
  private var a = ""

  def update(data: String) = {
    a = data
  }

  def encryptRSA(data: String): String = {
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.ENCRYPT_MODE, loadKeyArchivePubKey())

    val dataBytes = getBytesFromString(data)
    val cipheredData = cipher.doFinal(dataBytes)
    getStringFromBytes(cipheredData)
  }

  def encryptAES(data: String): String = {
    PlayCrypto.encryptAES(data, a)
  }

  def sign(key: PrivateKey, data: Array[Byte]): Array[Byte] = {
    val signer = Signature.getInstance("SHA1withRSA")
    signer.initSign(key)
    signer.update(data)
    signer.sign
  }

  def decodePrivateKey(encodedKey: String): PrivateKey = {
    val keyBytes = Crypto.getBytesFromString(encodedKey)
    getPrivateKeyFromBytes(keyBytes)
  }

  def generateKeyPair() = {
    val keyGen = KeyPairGenerator.getInstance("RSA")
    keyGen.initialize(1024)

    val keyPair = keyGen.genKeyPair()

    val privateKey = keyPair.getPrivate
    val publicKey = keyPair.getPublic

    val publicKeyBytes = publicKey.getEncoded
    val privateKeyBytes = privateKey.getEncoded

    val publicKeyString = Crypto.getStringFromBytes(publicKeyBytes)
    val privateKeyString = Crypto.getStringFromBytes(privateKeyBytes)

    (publicKeyString, privateKeyString)
  }

  def getBytesFromString(string: String): Array[Byte] = {
    string.toCharArray.map(_.toByte)
  }

  def getStringFromBytes(bytes: Array[Byte]): String = {
    new String(bytes.map(_.toChar))
  }

  def loadKeyArchivePubKey(): PublicKey = {
    getPublicKeyFromBytes(loadKeyFromFile("apublic.key"))
  }

  def loadKeyInterfacePvtKey(): PrivateKey = {
    getPrivateKeyFromBytes(loadKeyFromFile("iprivate.key"))
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

  def generateSymetricKey() = {
    val key = java.util.UUID.randomUUID.toString.filter(_ != '-')
    a = key
    a
  }
}
