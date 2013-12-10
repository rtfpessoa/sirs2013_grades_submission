package model

import scala.slick.driver.PostgresDriver.simple._
import model.traits.BaseTable

case class Student(id: Long, name: String, username: String)

object StudentFactory {
  val apply = Student.apply(-1, _: String, _: String)

  def unapply(s: Student) = {
    Some((s.name, s.username))
  }
}

object StudentTable extends Table[Student]("Student") with BaseTable[Student] {

  def name = column[String]("name")

  def username = column[String]("username")

  def * = id ~ name ~ username <>(Student, Student.unapply _)

  def auto = name ~ username <>(StudentFactory.apply, StudentFactory.unapply _)

  def getByUsername(username: String): Option[Student] = {
    BasicDB.withConnection {
      connection =>
        implicit session: Session =>
          (for (student <- this if student.username === username) yield student).list.headOption
    }
  }

}
