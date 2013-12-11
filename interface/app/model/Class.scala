package model

import scala.slick.driver.PostgresDriver.simple._
import model.traits.BaseTable

case class Course(id: Long, name: String, department: String)

object CourseFactory {
  val apply = Course.apply(-1, _: String, _: String)

  def unapply(c: Course) = {
    Some((c.name, c.department))
  }
}

object CourseTable extends Table[Course]("Class") with BaseTable[Course] {

  def name = column[String]("name")

  def department = column[String]("department")

  def * = id ~ name ~ department <>(Course, Course.unapply _)

  def auto = name ~ department <>(CourseFactory.apply, CourseFactory.unapply _)

}
