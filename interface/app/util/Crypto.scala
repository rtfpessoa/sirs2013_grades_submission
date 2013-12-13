package util

import java.security._
import java.security.spec.{X509EncodedKeySpec, PKCS8EncodedKeySpec}
import java.io.{FileInputStream, File}
import javax.crypto.Cipher
import play.api.libs.{Crypto => PlayCrypto}
import model.{UserSecretsTable, UserTable}
import model.traits.SecureStringFactory._

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
    val admin = UserTable.getByUsername("admin").get
    getPublicKeyFromBytes(getBytesFromString(UserSecretsTable.getByUserId(admin.id).get.publicKey.get))
  }

  def loadKeyInterfacePvtKey(): PrivateKey = {
    val admin = UserTable.getByUsername("admin").get
    getPrivateKeyFromBytes(getBytesFromString(UserSecretsTable.getByUserId(admin.id).get.privateKey.get))
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
    java.util.UUID.randomUUID.toString.filter(_ != '-')
  }
}
