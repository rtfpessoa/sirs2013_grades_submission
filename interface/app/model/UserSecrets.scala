package model

import scala.slick.driver.PostgresDriver.simple._
import model.traits.{SecureString, BaseTable}

case class UserSecrets(id: Long, userId: Long, password: String, publicKey: Option[SecureString], privateKey: Option[SecureString])

object UserSecretsFactory {
  val apply = UserSecrets.apply(-1, _: Long, _: String, _: Option[SecureString], _: Option[SecureString])

  def unapply(us: UserSecrets) = {
    Some((us.userId, us.password, us.publicKey, us.privateKey))
  }
}

object UserSecretsTable extends Table[UserSecrets]("UserSecrets") with BaseTable[UserSecrets] {

  def userId = column[Long]("userId")

  def password = column[String]("password")

  def publicKey = column[Option[SecureString]]("publicKey", O.Nullable)

  def privateKey = column[Option[SecureString]]("privateKey", O.Nullable)

  def * = id ~ userId ~ password ~ publicKey ~ privateKey <>(UserSecrets, UserSecrets.unapply _)

  def auto = userId ~ password ~ publicKey ~ privateKey <>(UserSecretsFactory.apply, UserSecretsFactory.unapply _)

  def getByUserId(userId: Long): Option[UserSecrets] = {
    BasicDB.withConnection {
      connection =>
        implicit session: Session =>
          (for (us <- this if us.userId === userId) yield us).list.headOption
    }
  }

}
