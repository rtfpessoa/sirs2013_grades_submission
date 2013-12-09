package model.traits

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.lifted.ColumnBase
import scala.language.reflectiveCalls
import model.BasicDB

trait BaseTable[A <: AnyRef {val id : Long}] extends SecureTable {
  this: Table[A] =>

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def * : scala.slick.lifted.ColumnBase[A]

  def auto: ColumnBase[A]

  def nonEmptyHead(list: List[A]): Option[A] = {
    list match {
      case l if !l.isEmpty => Some(l.head)
      case _ => None
    }
  }

  def getById(id: Long): Option[A] = {
    BasicDB.database.withSession {
      implicit session: Session =>
        val objs = (for (obj <- this if obj.id === id) yield obj).list
        nonEmptyHead(objs)
    }
  }

  def getAll: List[A] = {
    BasicDB.database.withSession {
      implicit session: Session =>
        (for (obj <- this sortBy (_.id)) yield obj).list
    }
  }

  def deleteById(id: Long) = {
    BasicDB.database.withSession {
      implicit session: Session =>
        this.where(_.id === id).delete
    }
  }

  def update(objectToUpdate: A) = {
    BasicDB.database.withSession {
      implicit session: Session =>
        (for (obj <- this if obj.id === objectToUpdate.id) yield obj).update(objectToUpdate)
    }
  }

  def create(objectToCreate: A): A = {
    BasicDB.database.withSession {
      implicit session: Session =>
        create(session, objectToCreate)
    }
  }

  def create(objectsToCreate: Seq[A]) = {
    BasicDB.database.withSession {
      implicit session: Session =>
        this.auto.insertAll(objectsToCreate: _*)
    }
  }

  def create(implicit session: Session, objectToCreate: A): A = {
    val id = {
      this.auto.insert(objectToCreate); Query(BasicDB.scopeIdentity).first
    }
    (for (obj <- this if obj.id === id) yield obj).list.head
  }

  def size(): Long = {
    BasicDB.database.withSession {
      implicit session: Session =>
        Query(this.length).first
    }
  }
}
