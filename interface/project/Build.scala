import sbt._
import play.Project._
import sbt.Keys._

object ApplicationBuild extends Build {

  val appName = "interface"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    jdbc,
    anorm,
    "com.typesafe.play" %% "play-slick" % "0.4.0",
    "org.postgresql" % "postgresql" % "9.2-1003-jdbc4"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalacOptions in ThisBuild += "-deprecation",
    scalacOptions in ThisBuild += "-feature",
    scalacOptions in ThisBuild += "-unchecked",
    scalacOptions in ThisBuild += "-Ywarn-adapted-args",
    scalacOptions in ThisBuild += "-Xlint"
  )

}
