package controllers.admin

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import controllers.traits.Secured
import rules.UserRules.Teacher
import rules.UserRules

object AdminController extends Controller with Secured {

  private val addTeacherForm = Form(
    mapping(
      "name" -> text,
      "username" -> text,
      "password" -> text
    )(Teacher.apply)(Teacher.unapply)
  )

  def index = withAdmin {
    admin => implicit request =>
      Ok(views.html.admin.index(admin))
  }

  def addTeacher = withAdmin {
    admin => implicit request =>
      addTeacherForm.bindFromRequest().fold(
      formWithErrors => {
        Ok(views.html.admin.addTeacher(admin))
      }, {
        case teacher: Teacher => {
          UserRules.createTeacher(teacher)
          Ok(views.html.admin.index(admin))
        }
      })
  }

}
