package controllers.admin

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import controllers.traits.Secured
import model._
import play.api.libs.json.Json

object AdminController extends Controller with Secured {

  private case class Teacher(name: String, username: String, password: String)

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
          BasicDB.database.withTransaction {
            val user = UserTable.create(UserFactory.apply(teacher.name, teacher.username, UserLevel.Teacher))
            UserSecretsTable.create(UserSecretsFactory.apply(user.id, teacher.password, None, None))
          }
          Ok(views.html.admin.index(admin))
        }
      })
  }

}
