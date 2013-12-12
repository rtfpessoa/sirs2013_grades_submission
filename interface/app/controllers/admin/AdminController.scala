package controllers.admin

import play.api.mvc._
import model._
import play.api.data._
import play.api.data.Forms._
import controllers.traits.Secured

object AdminController extends Controller with Secured {

  private val courseForm = Form(
    single(
      "courseId" -> number
    ))

  def index = withAdmin {
    admin => implicit request =>
      Ok(views.html.admin.index(admin))
  }

}
