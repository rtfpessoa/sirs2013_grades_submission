package model

import scala.slick.driver.PostgresDriver.simple._
import model.traits.{SecureString, BaseTable}

case class Teacher(id: Long, name: String, username: String, password: String, publicKey: Option[SecureString], privateKey: Option[SecureString])

object TeacherFactory {
  val apply = Teacher.apply(-1, _: String, _: String, _: String, _: Option[SecureString], _: Option[SecureString])

  def unapply(t: Teacher) = {
    Some((t.name, t.username, t.password, t.publicKey, t.privateKey))
  }
}

object TeacherTable extends Table[Teacher]("Teacher") with BaseTable[Teacher] {

  def name = column[String]("name")

  def username = column[String]("username")

  def password = column[String]("password")

  def publicKey = column[Option[SecureString]]("publicKey",O.Nullable)

  def privateKey = column[Option[SecureString]]("privateKey",O.Nullable)

  def * = id ~ name ~ username ~ password ~ publicKey ~ privateKey <>(Teacher, Teacher.unapply _)

  def auto = name ~ username ~ password ~ publicKey ~ privateKey <>(TeacherFactory.apply, TeacherFactory.unapply _)

  def getByUsername(username: String): Option[Teacher] = {
    BasicDB.withConnection {
      connection =>
        implicit session: Session =>
          (for (teacher <- this if teacher.username === username) yield teacher).list.headOption
    }
  }

}
