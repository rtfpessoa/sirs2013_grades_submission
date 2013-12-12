package controllers.admin

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import controllers.traits.Secured
import rules.UserRules.UserViewModel
import rules.{CourseRules, UserRules}
import model.{CourseFactory, CourseTable}
import rules.CourseRules.CourseViewModel

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
      Ok(views.html.admin.addTeacher(admin))
  }

  def addTeacherSubmit = withAdmin {
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
      Ok(views.html.admin.addStudent(admin))
  }

  def addStudentSubmit = withAdmin {
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

  private val addCourseForm = Form(
    mapping(
      "abbrev" -> text,
      "name" -> text,
      "department" -> text
    )(CourseViewModel.apply)(CourseViewModel.unapply)
  )

  def addCourse = withAdmin {
    admin => implicit request =>
      Ok(views.html.admin.addCourse(admin))
  }

  def addCourseSubmit = withAdmin {
    admin => implicit request =>
      addCourseForm.bindFromRequest().fold(
      formWithErrors => {
        Ok(views.html.admin.addCourse(admin))
      }, {
        case course: CourseViewModel => {
          CourseTable.create(CourseFactory.apply(course.abbrev, course.name, course.department))
          Ok(views.html.admin.index(admin))
        }
      })
  }

  def assignTeacherToCourse = TODO
  def assignTeacherToCourseSubmit = TODO

  def assignStudentToCourse = TODO
  def assignStudentToCourseSubmit = TODO

  def changeUserLevel = TODO
  def changeUserLevelSubmit = TODO

}
