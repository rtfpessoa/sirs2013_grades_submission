import sbt._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "archive"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    jdbc,
    "commons-io" % "commons-io" % "2.0",
    "play" %% "play" % "2.1.3" exclude("org.scala-lang", "scala-reflect"),
    "com.typesafe.play" %% "play-slick" % "0.5.0.8",
    "org.postgresql" % "postgresql" % "9.3-1100-jdbc4"
  )


  val main = play.Project(appName, appVersion, appDependencies)

}
