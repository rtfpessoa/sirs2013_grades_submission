package model

import scala.slick.driver.PostgresDriver.simple._
import model.traits.BaseTable

case class Course(id: Long, abbrev: String, name: String, department: String)

object CourseFactory {
  val apply = Course.apply(-1, _:String, _: String, _: String)

  def unapply(c: Course) = {
    Some((c.abbrev, c.name, c.department))
  }
}

object CourseTable extends Table[Course]("Course") with BaseTable[Course] {

  def abbrev = column[String]("abbrev")
  
  def name = column[String]("name")

  def department = column[String]("department")

  def * = id ~ abbrev ~ name ~ department <>(Course, Course.unapply _)

  def auto = abbrev ~ name ~ department <>(CourseFactory.apply, CourseFactory.unapply _)
  
  def getByAbbrev(abbrev: String): Option[Course] = {
    BasicDB.withConnection {
      connection =>
        implicit session: Session =>
          (for (course <- this if course.abbrev === abbrev) yield course).list.headOption
    }
  }

}
