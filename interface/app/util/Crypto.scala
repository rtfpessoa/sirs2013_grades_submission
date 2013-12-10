package util

import java.security.{KeyFactory, Signature, PrivateKey}
import java.security.spec.PKCS8EncodedKeySpec

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

}
