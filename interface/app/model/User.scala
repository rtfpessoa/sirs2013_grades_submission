package model

import scala.slick.driver.PostgresDriver.simple._
import model.traits.BaseTable

object UserLevel extends Enumeration {
  type Level = Value
  val Student, Teacher, Admin = Value
}

case class User(id: Long, name: String, username: String, level: UserLevel.Value)

object UserFactory {
  val apply = User.apply(-1, _: String, _: String, _: UserLevel.Value)

  def unapply(u: User) = {
    Some((u.name, u.username, u.level))
  }
}

object UserTable extends Table[User]("User") with BaseTable[User] {

  implicit val levelMapper = MappedTypeMapper.base[UserLevel.Value, String](
    level => level.toString,
    level => UserLevel.withName(level)
  )

  def name = column[String]("name")

  def username = column[String]("username")

  def level = column[UserLevel.Value]("level")

  def * = id ~ name ~ username ~ level <>(User, User.unapply _)

  def auto = name ~ username ~ level <>(UserFactory.apply, UserFactory.unapply _)

  def getByUsername(username: String): Option[User] = {
    BasicDB.withConnection {
      connection =>
        implicit session: Session =>
          (for (user <- this if user.username === username) yield user).list.headOption
    }
  }

}
