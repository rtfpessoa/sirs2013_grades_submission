package controllers.traits

import play.api.mvc._
import model._
import model.User

trait Secured {

  private def username(request: RequestHeader) = request.session.get(Security.username)

  private def onUnauthorized(request: RequestHeader) = Results.Redirect(controllers.routes.Auth.authenticate)

  private def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) {
      user =>
        Action(request => f(user)(request))
    }
  }

  def withTeacher(f: User => Request[AnyContent] => Result) = withAuth {
    username => implicit request =>
      UserTable.getByUsername(username).filter(_.level != UserLevel.Student).map {
        user =>
          f(user)(request)
      }.getOrElse(onUnauthorized(request))
  }

  def withAdmin(f: User => Request[AnyContent] => Result) = withAuth {
    username => implicit request =>
      UserTable.getByUsername(username).filter(_.level == UserLevel.Admin).map {
        user =>
          f(user)(request)
      }.getOrElse(onUnauthorized(request))
  }

}
