package controllers

import play.api.mvc._
import controllers.traits.Secured
import model.{ClassTable, Student, TeachingTable, EnrollmentTable}
import play.api.data._
import play.api.data.Forms._
import rules.ClassGrades
import rules.StudentGrade
import play.api.libs.json.Json

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

  private val gradesForm2 = (
    single(
      "grade" -> number
    ))

  def grades = Action {
    implicit request =>
      classForm.bindFromRequest.fold(
        formWithErrors => BadRequest(controllers.routes.Application.index().url),
        classId => {
          // TODO: Get grades from Archive

          Ok(views.html.grades(""))
        }
      )
  }

  def teacher = withTeacher {
    teacher => implicit request =>
      Ok(views.html.teacher(teacher, TeachingTable.getClassesOfTeacher(teacher.id)))
  }

  def selectCurse = withTeacher {
    teacher => implicit request =>
      classForm.bindFromRequest.fold(
        formWithErrors => BadRequest(controllers.routes.Application.teacher().url),
        classId => {
          Ok(views.html.assigngrades(teacher, ClassTable.getById(classId).get, EnrollmentTable.getStudentsOfClass(classId)))
        }
      )
  }

  val gradesForm = Form(
    mapping(
      "teacher" -> text,
      "clazz" -> text,
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

          Ok(Json.obj("success" ->
            s"""
            |${classGrades.className}
            |${classGrades.teacherUsername}
            |${classGrades.grades.mkString(",")}
          """.stripMargin))
        }
      })
  }
}
