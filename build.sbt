lazy val root = (project in file(".")).settings(
  name := "Request-Response Pattern",
  organization := "io.github.supersnager",
  version := "1.0.0",
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

  ),

  pomIncludeRepository := { _ => false },
  publishMavenStyle := true,
  sonatypeProfileName := "io.github.supersnager",
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  homepage := Some(url("https://supersnager.github.io/RequestResponsePattern")),
  scmInfo := Some(
    ScmInfo(
        url("https://github.com/supersnager/RequestResponsePattern"),
        "scm:git@github.com/supersnager/RequestResponsePattern.git"
      )
    ),
    developers := List(
    Developer(id="SuperSnager", name="SuperSnager", email="supersnager@gmail.com", url=url("https://github.com/supersnager"))
    )
)
