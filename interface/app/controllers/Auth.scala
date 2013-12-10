package controllers

import play.api.mvc.{Security, Action, Controller}
import model.TeacherTable
import util.MD5
import play.api.data._
import play.api.data.Forms._

object Auth extends Controller {

  def login = Action {
    implicit request =>
      Ok(views.html.login())
  }

  def logout = Action {
    Redirect(routes.Application.index()).withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }

  private val loginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
    ) verifying("Invalid email or password", result => result match {
      case (username, password) => check(username, password)
    })
  )

  private def check(username: String, password: String): Boolean = {
    val user = TeacherTable.getByUsername(username)

    if (user.isDefined) {
      user.get.password == MD5.hash(password)
    }
    else {
      false
    }
  }

  private def authenticate = Action {
    implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.login()),
        user => Redirect(routes.Application.index).withSession(Security.username -> user._1)
      )
  }

}
