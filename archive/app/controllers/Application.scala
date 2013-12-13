package controllers

import play.api.mvc._
import rules.storage.StorageRules

object Application extends Controller {

  case class IndexViewModel(courseAbbrev: String, courseName: String)

  def index = Action {
    if (StorageRules.gradesDir.exists()) {
      val courses = for (file <- StorageRules.gradesDir.listFiles if file != null && (file.getName endsWith "-grades.xml")) yield {
        val fileNameParts = file.getName.split("-")
        val courseAbbrev = fileNameParts(0)
        IndexViewModel(courseAbbrev, courseAbbrev)
      }
      Ok(views.html.index(courses))
    } else {
      Ok(views.html.index(Seq()))
    }
  }

}
