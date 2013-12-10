package controllers.crypto

import java.io._
import java.security._
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto._
import scala.Function.const

/**
 * User: rtfpessoa
 * Date: 10/12/13
 * Time: 11:57
 */

object Crypto {
  def encrypt(key: PublicKey, data: Array[Byte]): Array[Byte] = {
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.ENCRYPT_MODE, key)
    cipher.doFinal(data)
  }

  def decrypt(key: PrivateKey, data: Array[Byte]): Array[Byte] = {
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.DECRYPT_MODE, key)
    cipher.doFinal(data)
  }

  def sign(key: PrivateKey, data: Array[Byte]): Array[Byte] = {
    val signer = Signature.getInstance("SHA1withRSA")
    signer.initSign(key)
    signer.update(data)
    signer.sign
  }

  def verify(key: PublicKey, signature: Array[Byte], data: Array[Byte]): Boolean = {
    val verifier = Signature.getInstance("SHA1withRSA")
    verifier.initVerify(key)
    verifier.update(data)
    verifier.verify(signature)
  }

  def readPrivateKey(filePath: String): PrivateKey =
    decodePrivateKey(readEncodedRSAKey(filePath))

  def readPublicKey(filePath: String): PublicKey =
    decodePublicKey(readEncodedRSAKey(filePath))

  private def readEncodedRSAKey(filePath: String): Array[Byte] = {
    withDataInputStream(filePath) {
      stream =>
        val nameLength = stream.readInt
        stream.skip(nameLength)

        val keyLength = stream.readInt
        val key: Array[Byte] = Array.ofDim(keyLength)
        stream.read(key)

        key
    }
  }

  private def decodePrivateKey(encodedKey: Array[Byte]): PrivateKey = {
    val spec = new PKCS8EncodedKeySpec(encodedKey)
    val factory = KeyFactory.getInstance("RSA")
    factory.generatePrivate(spec)
  }

  private def decodePublicKey(encodedKey: Array[Byte]): PublicKey = {
    val spec = new X509EncodedKeySpec(encodedKey)
    val factory = KeyFactory.getInstance("RSA")
    factory.generatePublic(spec)
  }

  private def withCloseable[A <: Closeable, B](closeable: A)(f: A => B): B =
    const(f(closeable))(closeable.close)

  private def withDataInputStream[A](filePath: String): (DataInputStream => A) => A =
    withCloseable(new DataInputStream(new FileInputStream(filePath)))(_)
}
