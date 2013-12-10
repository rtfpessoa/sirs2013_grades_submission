package controllers

import play.api.mvc._
import controllers.traits.Secured
import model.{ClassTable, Student}
import play.api.data._
import play.api.data.Forms._

object Application extends Controller with Secured {

  def index = Action {
    val classes = ClassTable.getAll
    Ok(views.html.index(classes))
  }

  case class GradesViewModel(student: Student, grade: Long)

  private val classForm = Form(
    single(
      "classId" -> number
    ))

  def grades = Action {
    implicit request =>
      classForm.bindFromRequest.fold(
        formWithErrors => BadRequest(controllers.routes.Application.index().url),
        classId => {
          // TODO: Get grades from Archive

          Ok(views.html.grades(Seq()))
        }
      )
  }

  def teacher = withTeacher {
    teacher => implicit request =>
      Ok(views.html.teacher(teacher))
  }

}
