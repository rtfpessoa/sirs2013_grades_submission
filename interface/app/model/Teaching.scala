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

  def teacherId = column[Long]("teacherId")

  def classId = column[Long]("classId")

  def * = id ~ teacherId ~ classId <>(Teaching, Teaching.unapply _)

  def auto = teacherId ~ classId <>(TeachingFactory.apply, TeachingFactory.unapply _)

  def getClassesOfTeacher(teacherId: Long): Seq[Clazz] = {
    BasicDB.withConnection {
      connection =>
        implicit session: Session =>
          (for (teaching <- this if teaching.teacherId === teacherId) yield teaching.classId).list
    }.map {
    	classId => ClassTable.getById(classId)
    }.flatten
  }

}
