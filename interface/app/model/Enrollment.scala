package model

import scala.slick.driver.PostgresDriver.simple._
import model.traits.BaseTable

case class Enrollment(id: Long, studentId: Long, classId: Long)

object EnrollmentFactory {
  val apply = Enrollment.apply(-1, _: Long, _: Long)

  def unapply(s: Enrollment) = {
    Some((s.studentId, s.classId))
  }
}

object EnrollmentTable extends Table[Enrollment]("Enrollment") with BaseTable[Enrollment] {

  def studentId = column[Long]("studentId")

  def classId = column[Long]("classId")

  def * = id ~ studentId ~ classId <>(Enrollment, Enrollment.unapply _)

  def auto = studentId ~ classId <>(EnrollmentFactory.apply, EnrollmentFactory.unapply _)

}
