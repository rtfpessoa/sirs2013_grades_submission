package controllers

import play.api.mvc._
import controllers.traits.Secured
import model.{TeacherTable, CourseTable, TeachingTable, EnrollmentTable}
import play.api.data._
import play.api.data.Forms._
import rules.{CourseGrades, Archive, StudentGrade}
import play.api.libs.json.Json
import model.traits.SecureStringFactory

object Application extends Controller with Secured {

  private val classForm = Form(
    single(
      "courseId" -> number
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
      "courseId" -> longNumber,
      "courseName" -> text,
      "teacherName" -> text,
      "teacherUsername" -> text,
      "grades" -> seq(
        mapping(
          "name" -> text,
          "username" -> text,
          "grade" -> number
        )(StudentGrade.apply)(StudentGrade.unapply)
      )
    )(CourseGrades.apply)(CourseGrades.unapply)
  )

  def submitGrades = Action {
    implicit request =>
      gradesForm.bindFromRequest().fold(
      formWithErrors => {
        Ok(Json.obj("error" -> formWithErrors.errorsAsJson))
      }, {
        case classGrades: CourseGrades => {

          val response = Archive.sendGrades(classGrades)

          Ok(Json.obj("success" -> response))
        }
      })
  }

  def supplyKey(username: String) = Action {
    implicit request =>
      val publicKey = TeacherTable.getByUsername(username).get.publicKey
      val decipheredPublicKey = SecureStringFactory.fromSecureString(publicKey.get)

      Ok(Json.obj("key" -> decipheredPublicKey))
  }

}
