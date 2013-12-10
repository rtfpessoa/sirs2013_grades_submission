package controllers.traits

import play.api.mvc._
import model.{Teacher, TeacherTable}

trait Secured {

  private def username(request: RequestHeader) = request.session.get(Security.username)

  private def onUnauthorized(request: RequestHeader) = Results.Redirect(controllers.routes.Auth.authenticate)

  private def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) {
      user =>
        Action(request => f(user)(request))
    }
  }

  def withTeacher(f: Teacher => Request[AnyContent] => Result) = withAuth {
    username => implicit request =>
      TeacherTable.getByUsername(username).map {
        user =>
          f(user)(request)
      }.getOrElse(onUnauthorized(request))
  }

}
