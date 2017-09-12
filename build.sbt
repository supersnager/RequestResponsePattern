lazy val root = (project in file(".")).settings(
  name := "Tools Request-Response Pattern",
  organization := "crms",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.11.11",
  scalacOptions += "-deprecation",
  scalacOptions += "-target:jvm-1.8",
  scalacOptions += "-feature",
  libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.11",
  "com.typesafe.akka" %% "akka-actor" % "2.5.2",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.2" % Test
  )
)
