package controllers

import play.api.mvc._
import rules.storage.StorageRules

object Application extends Controller {

  case class IndexViewModel(courseId: Int, courseName: String)

  def index = Action {
    if (StorageRules.gradesDir.exists()) {
      val courses = for (file <- StorageRules.gradesDir.listFiles if file != null && (file.getName endsWith "-grades.txt")) yield {
        val fileNameParts = file.getName.split("-")
        val courseId = fileNameParts(0).toInt
        IndexViewModel(courseId, "Nome" + courseId)
      }
      Ok(views.html.index(courses))
    } else {
      Ok(views.html.index(Seq()))
    }
  }

}
