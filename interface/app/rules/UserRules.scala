package rules

import model._
import model.traits.SecureStringFactory._
import util.{MD5, Crypto}

object UserRules {

  case class UserViewModel(name: String, username: String, password: String)

  def createAdmin(admin: UserViewModel) = {
    BasicDB.database.withTransaction {
      val user = UserTable.create(UserFactory.apply(admin.name, admin.username, UserLevel.Admin))
      val (publicKey, privateKey) = Crypto.generateKeyPair()
      UserSecretsTable.create(UserSecretsFactory.apply(user.id, MD5.hash(admin.password), Some(publicKey), Some(privateKey)))
    }
  }

  def createTeacher(teacher: UserViewModel) = {
    BasicDB.database.withTransaction {
      val user = UserTable.create(UserFactory.apply(teacher.name, teacher.username, UserLevel.Teacher))
      val (publicKey, privateKey) = Crypto.generateKeyPair()
      UserSecretsTable.create(UserSecretsFactory.apply(user.id, MD5.hash(teacher.password), Some(publicKey), Some(privateKey)))
    }
  }

  def createStudent(student: UserViewModel) = {
    BasicDB.database.withTransaction {
      val user = UserTable.create(UserFactory.apply(student.name, student.username, UserLevel.Student))
      UserSecretsTable.create(UserSecretsFactory.apply(user.id, MD5.hash(student.password), None, None))
    }
  }

  def getAllTeachers: Seq[User] = {
    UserTable.getAll.filter(_.level == UserLevel.Teacher)
  }

  def getAllStudents: Seq[User] = {
    UserTable.getAll.filter(_.level == UserLevel.Student)
  }

}
