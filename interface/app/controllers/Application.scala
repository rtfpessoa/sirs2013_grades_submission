package controllers

import play.api.mvc._
import controllers.traits.Secured
import model.{CourseTable, TeachingTable, EnrollmentTable}
import play.api.data._
import play.api.data.Forms._
import rules.{Archive, ClassGrades, StudentGrade}
import play.api.libs.json.Json

object Application extends Controller with Secured {

  private val classForm = Form(
    single(
      "classId" -> number
    ))

  def index = withTeacher {
    teacher => implicit request =>
      Ok(views.html.index(teacher, TeachingTable.getClassesOfTeacher(teacher.id)))
  }

  def selectCourse = withTeacher {
    teacher => implicit request =>
      classForm.bindFromRequest.fold(
        formWithErrors => BadRequest(controllers.routes.Application.index().url),
        classId => {
          Ok(views.html.assigngrades(teacher, CourseTable.getById(classId).get, EnrollmentTable.getStudentsOfClass(classId)))
        }
      )
  }

  val gradesForm = Form(
    mapping(
      "teacher" -> text,
      "course" -> text,
      "grades" -> seq(
        mapping(
          "username" -> text,
          "grade" -> number
        )(StudentGrade.apply)(StudentGrade.unapply)
      )
    )(ClassGrades.apply)(ClassGrades.unapply)
  )

  def submitGrades = Action {
    implicit request =>
      gradesForm.bindFromRequest().fold(
      formWithErrors => {
        Ok(Json.obj("error" -> formWithErrors.errorsAsJson))
      }, {
        case classGrades: ClassGrades => {

          Archive.sendGrades(classGrades)

          Ok(Json.obj("success" -> ""))
        }
      })
  }
}
