package controllers

import play.api.mvc.{Security, Action, Controller}
import model.TeacherTable
import util.MD5

object Auth extends Controller {

  def login = Action {
    implicit request =>
      val uri = controllers.routes.Application.index()
      val username = request.getQueryString("username")
      val password = request.getQueryString("password")

      if (username.isEmpty || password.isEmpty) {
        Ok(views.html.login())
      }
      else if (validUser(username.get, password.get)) {
        Redirect(uri).withSession(
          request.session - "access_uri" + (Security.username -> username.get))
      } else {
        Redirect(controllers.routes.Auth.login).withNewSession.flashing(
          "success" -> "No email found. Please use another provider.")
      }
  }

  def logout = Action {
    implicit request =>
      Redirect(controllers.routes.Application.index()).withNewSession.flashing(
        "success" -> "You are now logged out.")
  }

  private def validUser(username: String, password: String): Boolean = {
    val user = TeacherTable.getByUsername(username)

    if (user.isDefined) {
      user.get.password == MD5.hash(password)
    }
    else {
      false
    }
  }

}
