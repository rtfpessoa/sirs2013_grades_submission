package util

import java.security._
import java.security.spec.PKCS8EncodedKeySpec

object Crypto {

  def sign(key: PrivateKey, data: Array[Byte]): Array[Byte] = {
    val signer = Signature.getInstance("SHA1withRSA")
    signer.initSign(key)
    signer.update(data)
    signer.sign
  }

  def decodePrivateKey(encodedKey: String): PrivateKey = {
    val keyBytes = Crypto.getBytesFromString(encodedKey)
    val spec = new PKCS8EncodedKeySpec(keyBytes)
    val factory = KeyFactory.getInstance("RSA")
    factory.generatePrivate(spec)
  }

  def generateKeyPair() = {
    val keyGen: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")

    keyGen.initialize(1024)
    val keyPair: KeyPair = keyGen.genKeyPair()

    val privateKey: PrivateKey = keyPair.getPrivate
    val publicKey: PublicKey = keyPair.getPublic

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
}
