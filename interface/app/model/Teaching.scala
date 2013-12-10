package model

import scala.slick.driver.PostgresDriver.simple._
import model.traits.BaseTable

case class Teaching(id: Long, teacherId: Long, classId: Long)

object TeachingFactory {
  val apply = Teaching.apply(-1, _: Long, _: Long)

  def unapply(t: Teaching) = {
    Some((t.teacherId, t.classId))
  }
}

object TeachingTable extends Table[Teaching]("Teaching") with BaseTable[Teaching] {

  def teachingId = column[Long]("teachingId")

  def classId = column[Long]("classId")

  def * = id ~ teachingId ~ classId <>(Teaching, Teaching.unapply _)

  def auto = teachingId ~ classId <>(TeachingFactory.apply, TeachingFactory.unapply _)

}
