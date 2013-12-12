package controllers.admin

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import controllers.traits.Secured
import rules.UserRules.UserViewModel
import rules.{CourseRules, UserRules}
import model._
import rules.CourseRules.{EnrollmentViewModel, TeachingViewModel, CourseViewModel}
import rules.CourseRules.CourseViewModel
import rules.UserRules.UserViewModel
import rules.CourseRules.EnrollmentViewModel
import rules.CourseRules.TeachingViewModel

object AdminController extends Controller with Secured {

  private val addUserForm = Form(
    mapping(
      "name" -> text,
      "username" -> text,
      "password" -> text
    )(UserViewModel.apply)(UserViewModel.unapply)
  )

  def index = withAdmin {
    admin => implicit request =>
      Ok(views.html.admin.index(admin))
  }

  def addTeacher = withAdmin {
    admin => implicit request =>
      Ok(views.html.admin.addTeacher(admin))
  }

  def addTeacherSubmit = withAdmin {
    admin => implicit request =>
      addUserForm.bindFromRequest().fold(
      formWithErrors => {
        Ok(views.html.admin.addTeacher(admin))
      }, {
        case teacher: UserViewModel => {
          UserRules.createTeacher(teacher)
          Ok(views.html.admin.index(admin))
        }
      })
  }

  def addStudent = withAdmin {
    admin => implicit request =>
      Ok(views.html.admin.addStudent(admin))
  }

  def addStudentSubmit = withAdmin {
    admin => implicit request =>
      addUserForm.bindFromRequest().fold(
      formWithErrors => {
        Ok(views.html.admin.addStudent(admin))
      }, {
        case student: UserViewModel => {
          UserRules.createStudent(student)
          Ok(views.html.admin.index(admin))
        }
      })
  }

  private val addCourseForm = Form(
    mapping(
      "abbrev" -> text,
      "name" -> text,
      "department" -> text
    )(CourseViewModel.apply)(CourseViewModel.unapply)
  )

  def addCourse = withAdmin {
    admin => implicit request =>
      Ok(views.html.admin.addCourse(admin))
  }

  def addCourseSubmit = withAdmin {
    admin => implicit request =>
      addCourseForm.bindFromRequest().fold(
      formWithErrors => {
        Ok(views.html.admin.addCourse(admin))
      }, {
        case course: CourseViewModel => {
          CourseTable.create(CourseFactory.apply(course.abbrev, course.name, course.department))
          Ok(views.html.admin.index(admin))
        }
      })
  }

  private val assignTeacherToCourseForm = Form(
    mapping(
      "teacherId" -> longNumber,
      "courseId" -> longNumber
    )(TeachingViewModel.apply)(TeachingViewModel.unapply)
  )

  def assignTeacherToCourse = withAdmin {
    admin => implicit request =>
      val teachers = UserRules.getAllTeachers
      val courses = CourseTable.getAll
      Ok(views.html.admin.assignTeacherToCourse(admin, teachers, courses))
  }

  def assignTeacherToCourseSubmit = withAdmin {
    admin => implicit request =>
      assignTeacherToCourseForm.bindFromRequest().fold(
      formWithErrors => {
        val teachers = UserRules.getAllTeachers
        val courses = CourseTable.getAll
        Ok(views.html.admin.assignTeacherToCourse(admin, teachers, courses))
      }, {
        case teaching: TeachingViewModel => {
          TeachingTable.create(TeachingFactory.apply(teaching.teacherId, teaching.courseId))
          Ok(views.html.admin.index(admin))
        }
      })
  }

  private val assignStudentToCourseForm = Form(
    mapping(
      "studentId" -> longNumber,
      "courseId" -> longNumber
    )(EnrollmentViewModel.apply)(EnrollmentViewModel.unapply)
  )

  def assignStudentToCourse = withAdmin {
    admin => implicit request =>
      val students = UserRules.getAllStudents
      val courses = CourseTable.getAll
      Ok(views.html.admin.assignStudentToCourse(admin, students, courses))
  }

  def assignStudentToCourseSubmit = withAdmin {
    admin => implicit request =>
      assignStudentToCourseForm.bindFromRequest().fold(
      formWithErrors => {
        val students = UserRules.getAllStudents
        val courses = CourseTable.getAll
        Ok(views.html.admin.assignStudentToCourse(admin, students, courses))
      }, {
        case enrollment: EnrollmentViewModel => {
          EnrollmentTable.create(EnrollmentFactory.apply(enrollment.studentId, enrollment.courseId))
          Ok(views.html.admin.index(admin))
        }
      })
  }

  private case class UserLevelViewModel(userId: Long, level: String)

  private val changeUserLevelForm = Form(
    mapping(
      "userId" -> longNumber,
      "level" -> text
    )(UserLevelViewModel.apply)(UserLevelViewModel.unapply)
  )

  def changeUserLevel = withAdmin {
    admin => implicit request =>
      val users = UserTable.getAll
      val levels = Seq(UserLevel.Student, UserLevel.Teacher, UserLevel.Admin)
      Ok(views.html.admin.changeUserLevel(admin, users, levels))
  }

  def changeUserLevelSubmit = withAdmin {
    admin => implicit request =>
      changeUserLevelForm.bindFromRequest().fold(
      formWithErrors => {
        val users = UserTable.getAll
        val levels = Seq(UserLevel.Student, UserLevel.Teacher, UserLevel.Admin)
        Ok(views.html.admin.changeUserLevel(admin, users, levels))
      }, {
        case userLevel: UserLevelViewModel => {
          val user = UserTable.getById(userLevel.userId).get
          UserTable.update(User(user.id, user.name, user.username, UserLevel.withName(userLevel.level)))
          Ok(views.html.admin.index(admin))
        }
      })
  }

}
