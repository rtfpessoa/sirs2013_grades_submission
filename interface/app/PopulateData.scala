import model._
import rules.{ UserRules, CourseRules }

object PopulateData {

  def populate = {
    if (UserTable.getByUsername("ist169801").isEmpty) {
      UserRules.createAdmin(UserRules.UserViewModel("Rafael Cortês", "ist169801", "password"))
      UserRules.createTeacher(UserRules.UserViewModel("Paulo Marques", "ist169298", "password"))
      UserRules.createTeacher(UserRules.UserViewModel("Rodrigo Fernandes", "ist169637", "password"))
      UserRules.createTeacher(UserRules.UserViewModel("João Rodrigues", "ist169408", "password"))
      UserRules.createTeacher(UserRules.UserViewModel("André Bico", "ist169657", "password"))
      UserRules.createTeacher(UserRules.UserViewModel("Manuel Almeida", "ist120000", "password"))
      UserRules.createTeacher(UserRules.UserViewModel("José Severino", "ist120001", "password"))
      UserRules.createTeacher(UserRules.UserViewModel("José Esteves", "ist120002", "password"))
      UserRules.createTeacher(UserRules.UserViewModel("Manuel Lopes da Silva", "ist120003", "password"))
      UserRules.createTeacher(UserRules.UserViewModel("José Carlos Lopes da Silva", "ist120004", "password"))
      UserRules.createTeacher(UserRules.UserViewModel("António Chora Barroso", "ist120005", "password"))

      UserRules.createStudent(UserRules.UserViewModel("Bart Simpson", "ist190000", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Homer Simpson", "ist190001", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Marge Simpson", "ist190002", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Lisa Simpson", "ist190003", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Maggie Simpson", "ist190004", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Patty Bouvier", "ist190005", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Selma Bouvier", "ist190006", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Herbert Powell", "ist190007", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Abraham Simpson", "ist190008", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Ned Flanders", "ist190009", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Maude Flanders", "ist190010", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Rod Flanders", "ist190011", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Tod Flanders", "ist190012", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Charles Montgomery Burns", "ist190013", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Waylon Smithers", "ist190014", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Seymour Skinner", "ist190015", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Edna Krabappel", "ist190016", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Barney Gumble", "ist190017", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Nelson Muntz", "ist190018", "password"))
      UserRules.createStudent(UserRules.UserViewModel("Fat Tony", "ist190019", "password"))

      CourseRules.createCourse(CourseRules.CourseViewModel("SIRS", "Segurança Informática em Redes e Sistemas", "DEI"))
      CourseRules.createCourse(CourseRules.CourseViewModel("CPD", "Computação Paralela e Distribuída", "DEI"))
      CourseRules.createCourse(CourseRules.CourseViewModel("ASof", "Arquitecturas de Software", "DEI"))
      CourseRules.createCourse(CourseRules.CourseViewModel("QS", "Qualidade de Software", "DEI"))
      CourseRules.createCourse(CourseRules.CourseViewModel("GPI", "Gestão de Projectos Informáticos", "DEI"))

      CourseRules.assignTeacher(CourseRules.TeachingViewModel(UserTable.getByUsername("ist169298").get.id,
        CourseTable.getByAbbrev("SIRS").get.id))
      CourseRules.assignTeacher(CourseRules.TeachingViewModel(UserTable.getByUsername("ist169637").get.id,
        CourseTable.getByAbbrev("SIRS").get.id))
      CourseRules.assignTeacher(CourseRules.TeachingViewModel(UserTable.getByUsername("ist169408").get.id,
        CourseTable.getByAbbrev("CPD").get.id))
      CourseRules.assignTeacher(CourseRules.TeachingViewModel(UserTable.getByUsername("ist169657").get.id,
        CourseTable.getByAbbrev("CPD").get.id))
      CourseRules.assignTeacher(CourseRules.TeachingViewModel(UserTable.getByUsername("ist120000").get.id,
        CourseTable.getByAbbrev("ASof").get.id))
      CourseRules.assignTeacher(CourseRules.TeachingViewModel(UserTable.getByUsername("ist120001").get.id,
        CourseTable.getByAbbrev("ASof").get.id))
      CourseRules.assignTeacher(CourseRules.TeachingViewModel(UserTable.getByUsername("ist120002").get.id,
        CourseTable.getByAbbrev("QS").get.id))
      CourseRules.assignTeacher(CourseRules.TeachingViewModel(UserTable.getByUsername("ist120003").get.id,
        CourseTable.getByAbbrev("QS").get.id))
      CourseRules.assignTeacher(CourseRules.TeachingViewModel(UserTable.getByUsername("ist120004").get.id,
        CourseTable.getByAbbrev("GPI").get.id))
      CourseRules.assignTeacher(CourseRules.TeachingViewModel(UserTable.getByUsername("ist120005").get.id,
        CourseTable.getByAbbrev("GPI").get.id))

      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190000").get.id,
        CourseTable.getByAbbrev("SIRS").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190001").get.id,
        CourseTable.getByAbbrev("SIRS").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190002").get.id,
        CourseTable.getByAbbrev("SIRS").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190003").get.id,
        CourseTable.getByAbbrev("SIRS").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190004").get.id,
        CourseTable.getByAbbrev("CPD").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190005").get.id,
        CourseTable.getByAbbrev("CPD").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190006").get.id,
        CourseTable.getByAbbrev("CPD").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190007").get.id,
        CourseTable.getByAbbrev("CPD").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190008").get.id,
        CourseTable.getByAbbrev("ASof").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190009").get.id,
        CourseTable.getByAbbrev("ASof").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190010").get.id,
        CourseTable.getByAbbrev("ASof").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190011").get.id,
        CourseTable.getByAbbrev("ASof").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190012").get.id,
        CourseTable.getByAbbrev("QS").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190013").get.id,
        CourseTable.getByAbbrev("QS").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190014").get.id,
        CourseTable.getByAbbrev("QS").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190015").get.id,
        CourseTable.getByAbbrev("QS").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190016").get.id,
        CourseTable.getByAbbrev("GPI").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190017").get.id,
        CourseTable.getByAbbrev("GPI").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190018").get.id,
        CourseTable.getByAbbrev("GPI").get.id))
      CourseRules.enrollStudent(CourseRules.EnrollmentViewModel(UserTable.getByUsername("ist190019").get.id,
        CourseTable.getByAbbrev("GPI").get.id))
    }
  }
}