package controllers.storage

import play.api.mvc._

/**
 * User: rtfpessoa
 * Date: 10/12/13
 * Time: 11:40
 */
object StorageController extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def receive = Action {
    request =>
      val payload = request.getQueryString("payload")

      val signedGrades = payload.map {
        cipheredGrades =>
        //TODO: decipher grades
      }

      //TODO: store grade in file

      Ok(views.html.index("Your new application is ready."))
  }
}