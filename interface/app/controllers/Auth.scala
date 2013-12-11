package controllers

import play.api.mvc.{Security, Action, Controller}
import model.{UserSecretsTable, UserTable}
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
    val user = UserTable.getByUsername(username)

    if (user.isDefined) {
      val secrets = UserSecretsTable.getByUserId(user.get.id).get
      secrets.password == MD5.hash(password)
    }
    else {
      false
    }
  }

  def authenticate = Action {
    implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.login()),
        user => Redirect(routes.Application.index()).withSession(Security.username -> user._1)
      )
  }

}
