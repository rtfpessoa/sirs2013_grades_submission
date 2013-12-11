package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import rules.StorageRules.StorageRules

object Application extends Controller {

  case class IndexViewModel(courseId: Int, courseName: String)

  def index = Action {
    if (StorageRules.gradesDir.exists()) {
      val courses = for (file <- StorageRules.gradesDir.listFiles if file != null && (file.getName endsWith "-grades.txt")) yield {
        val fileNameParts = file.getName.split("-")
        val courseId = fileNameParts(0).toInt
        IndexViewModel(courseId, "Nome")
      }
      Ok(views.html.index(courses))
    } else {
      Ok(views.html.index(Seq()))
    }
  }

  case class GradesViewModel(student: String, grade: Long)

  private val classForm = Form(
    single(
      "classId" -> number
    ))

  def grades = Action {
    implicit request =>
      classForm.bindFromRequest.fold(
        formWithErrors => BadRequest(controllers.routes.Application.index().url),
        courseName => {
          /*val gradesFile = StorageRules.getGradesFile(courseName.split("-")[0], courseName.split("-")[1])*/
          Ok(views.html.grades(""))
        }
      )
  }

}