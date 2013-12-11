package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import scala.collection.JavaConversions._
import java.io.{File}
import java.util.{Scanner}

object Application extends Controller {

  private val courseForm = Form(
    single(
      "courseName" -> text
    ))

  def index = Action {
    /*val classes = for(file <- StorageRules.gradesDir.listFiles if file.getName endsWith "-grades.txt") yield {
      val fileNameParts = file.getname().split("-")
      val course = fileNameParts[0]
      val teacher = fileNameParts[1]
      course + "-" + teacher
    }*/
    Ok(views.html.index(Seq("")))
  }

  case class GradesViewModel(student: String, grade: Long)

  private val classForm = Form(
    single(
      "classId" -> number
    ))

  def grades = Action {
    implicit request =>
      classForm.bindFromRequest.fold(
        formWithErrors => BadRequest(controllers.routes.Application.index().url),
        courseName => {
          /*val gradesFile = StorageRules.getGradesFile(courseName.split("-")[0], courseName.split("-")[1])*/
          Ok(views.html.grades(""))
        }
      )
  }
}