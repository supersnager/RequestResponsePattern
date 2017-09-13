lazy val root = (project in file(".")).settings(
  name := "Request-Response Pattern",
  organization := "crms.tools",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.11.11",
  scalacOptions += "-deprecation",
  scalacOptions += "-target:jvm-1.8",
  scalacOptions += "-feature",
  libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.11",
  "com.typesafe.akka" %% "akka-actor" % "2.5.2",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.2" % Test,
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"

  )
)
