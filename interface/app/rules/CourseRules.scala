package rules

import model._

object CourseRules {

  case class CourseViewModel(abbrev: String, name: String, department: String)

  case class TeachingViewModel(teacherId: Long, courseId: Long)
  
  case class EnrollmentViewModel(studentId: Long, courseId: Long)

  def createCourse(course: CourseViewModel) = {
    BasicDB.database.withTransaction {
      CourseTable.create(CourseFactory.apply(course.abbrev, course.name, course.department))
    }
  }

  def assignTeacher(teaching: TeachingViewModel) = {
    BasicDB.database.withTransaction {
      TeachingTable.create(TeachingFactory.apply(teaching.teacherId, teaching.courseId))
    }
  }
  
  def enrollStudent(enrollment: EnrollmentViewModel) = {
    BasicDB.database.withTransaction {
      EnrollmentTable.create(EnrollmentFactory.apply(enrollment.studentId, enrollment.courseId))
    }
  }

}