package util

import java.security._
import java.security.spec.{X509EncodedKeySpec, PKCS8EncodedKeySpec}
import scala.Predef.String

object Crypto {

  def sign(key: PrivateKey, data: Array[Byte]): Array[Byte] = {
    val signer = Signature.getInstance("SHA1withRSA")
    signer.initSign(key)
    signer.update(data)
    signer.sign
  }

  def decodePrivateKey(encodedKey: String): PrivateKey = {
    val keyBytes = encodedKey.toCharArray.map(_.toByte)
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

    val x509EncodedKeySpec: X509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded)
    val pkcs8EncodedKeySpec: PKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded)

    val publicKeyString = new String(x509EncodedKeySpec.getEncoded.map(_.toChar))
    val privateKeyString = new String(pkcs8EncodedKeySpec.getEncoded.map(_.toChar))

    (publicKeyString, privateKeyString)
  }

}
