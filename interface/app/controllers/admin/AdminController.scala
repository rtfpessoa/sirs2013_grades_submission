package controllers.admin

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import controllers.traits.Secured
import rules.UserRules.UserViewModel
import rules.UserRules

object AdminController extends Controller with Secured {

  private val addUserForm = Form(
    mapping(
      "name" -> text,
      "username" -> text,
      "password" -> text
    )(UserViewModel.apply)(UserViewModel.unapply)
  )

  def index = withAdmin {
    admin => implicit request =>
      Ok(views.html.admin.index(admin))
  }

  def addTeacher = withAdmin {
    admin => implicit request =>
      addUserForm.bindFromRequest().fold(
      formWithErrors => {
        Ok(views.html.admin.addTeacher(admin))
      }, {
        case teacher: UserViewModel => {
          UserRules.createTeacher(teacher)
          Ok(views.html.admin.index(admin))
        }
      })
  }

  def addStudent = withAdmin {
    admin => implicit request =>
      addUserForm.bindFromRequest().fold(
      formWithErrors => {
        Ok(views.html.admin.addStudent(admin))
      }, {
        case student: UserViewModel => {
          UserRules.createStudent(student)
          Ok(views.html.admin.index(admin))
        }
      })
  }

  def addCourse = TODO

  def assignTeacherToCourse = TODO

  def assignStudentToCourse = TODO

  def changeUserLevel = TODO

}
