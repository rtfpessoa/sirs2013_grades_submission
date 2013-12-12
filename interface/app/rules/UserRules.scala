package rules

import model._
import model.traits.SecureStringFactory._
import util.Crypto

object UserRules {

  case class Teacher(name: String, username: String, password: String)

  def createTeacher(teacher: Teacher) = {
    BasicDB.database.withTransaction {
      val user = UserTable.create(UserFactory.apply(teacher.name, teacher.username, UserLevel.Teacher))
      val (publicKey, privateKey) = Crypto.generateKeyPair()
      UserSecretsTable.create(UserSecretsFactory.apply(user.id, teacher.password, Some(publicKey), Some(privateKey)))
    }
  }
}
