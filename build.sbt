val AkkaVersion = "2.6.14"

lazy val root = (project in file(".")).settings(
  name := "Request-Response Pattern",
  organization := "io.github.supersnager",
  version := "1.0.0",
  //scalaVersion := "2.12.7",
  scalaVersion := "2.13.5",
  scalacOptions += "-deprecation",
  scalacOptions += "-target:jvm-1.8",
  scalacOptions += "-feature",
  resolvers += "Artima Maven Repository" at "https://repo.artima.com/releases",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test,
    "org.scalactic" %% "scalactic" % "3.2.7",
    "org.scalatest" %% "scalatest" % "3.2.7" % Test

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
