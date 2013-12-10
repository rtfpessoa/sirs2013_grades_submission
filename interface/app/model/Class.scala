package model

import scala.slick.driver.PostgresDriver.simple._
import model.traits.BaseTable

case class Clazz(id: Long, name: String, department: String)

object ClassFactory {
  val apply = Clazz.apply(-1, _: String, _: String)

  def unapply(c: Clazz) = {
    Some((c.name, c.department))
  }
}

object ClassTable extends Table[Clazz]("Class") with BaseTable[Clazz] {

  def name = column[String]("name")

  def department = column[String]("department")

  def * = id ~ name ~ department <>(Clazz, Clazz.unapply _)

  def auto = name ~ department <>(ClassFactory.apply, ClassFactory.unapply _)

}
