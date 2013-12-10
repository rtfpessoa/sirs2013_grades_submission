package controllers

import play.api._
import play.api.mvc._
import controllers.traits.Secured

object Application extends Controller with Secured {

  def index = withTeacher {
    teacher => implicit request =>
      Ok(views.html.index("Your new application is ready."))
  }

}
