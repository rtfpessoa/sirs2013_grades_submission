package model.traits

import scala.slick.driver.PostgresDriver.simple._
import play.api.libs.Crypto
import scala.language.reflectiveCalls

case class SecureString(str: String) {

  override def toString: String = str

  def cipher: String = Crypto.encryptAES(str)

}

object SecureStringFactory {

  def decipher(str: String): SecureString = SecureString(Crypto.decryptAES(str))

}

trait SecureTable {

  implicit val secureStringMapper = MappedTypeMapper.base[SecureString, String](
  {
    secure => secure.cipher
  }, {
    string => SecureStringFactory.decipher(string)
  })

}
