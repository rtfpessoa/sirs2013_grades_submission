import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "interface"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "play" %% "play" % "2.1.3" exclude("org.scala-lang", "scala-reflect"),
    "com.typesafe.play" %% "play-slick" % "0.5.0.8",
    "org.postgresql" % "postgresql" % "9.2-1003-jdbc4"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
