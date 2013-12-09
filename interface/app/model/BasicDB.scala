package model

import play.api.Play.current
import play.api.db.DB
import scala.slick.driver.PostgresDriver.simple._


object BasicDB {

  def database = Database.forDataSource(DB.getDataSource())

  def withConnection[A](block: (java.sql.Connection) => Session => A): A = {
    this.database.withSession {
      DB.withConnection(block)
    }
  }

  def withTransaction[A](block: (java.sql.Connection) => Session => A): A = {
    this.database.withSession {
      DB.withTransaction(block)
    }
  }

  val scopeIdentity = SimpleFunction.nullary[Long]("LASTVAL")
}
