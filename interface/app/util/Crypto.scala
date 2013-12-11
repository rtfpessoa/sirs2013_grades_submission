package util

import java.security._
import java.security.spec.{X509EncodedKeySpec, PKCS8EncodedKeySpec}

object Crypto {

  def sign(key: PrivateKey, data: Array[Byte]): Array[Byte] = {
    val signer = Signature.getInstance("SHA1withRSA")
    signer.initSign(key)
    signer.update(data)
    signer.sign
  }

  def decodePrivateKey(encodedKey: Array[Byte]): PrivateKey = {
    val spec = new PKCS8EncodedKeySpec(encodedKey)
    val factory = KeyFactory.getInstance("RSA")
    factory.generatePrivate(spec)
  }

  def generateKeyPair() = {
    val keyGen: KeyPairGenerator = KeyPairGenerator.getInstance("DSA")

    keyGen.initialize(1024)
    val keyPair: KeyPair = keyGen.genKeyPair()

    val privateKey: PrivateKey = keyPair.getPrivate
    val publicKey: PublicKey = keyPair.getPublic

    val x509EncodedKeySpec: X509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded)

    val pkcs8EncodedKeySpec: PKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded)
    (new String(x509EncodedKeySpec.getEncoded), new String(pkcs8EncodedKeySpec.getEncoded))
  }

}
