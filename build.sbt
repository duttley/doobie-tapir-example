val Http4sVersion = "0.20.8"
val CirceVersion = "0.11.1"
val Specs2Version = "4.1.0"
val LogbackVersion = "1.2.3"
val tapirVersion = "0.11.9"
lazy val doobieVersion = "0.8.4"

lazy val root = (project in file("."))
  .settings(
    organization := "com.dutt",
    name := "schema-reg",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "org.specs2" %% "specs2-core" % Specs2Version % "test",
      "com.softwaremill.tapir" %% "tapir-core" % tapirVersion,
      "com.softwaremill.tapir" %% "tapir-http4s-server" % tapirVersion,
      "com.softwaremill.tapir" %% "tapir-json-circe" % tapirVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.http4s" %% "http4s-server" % Http4sVersion,
      "ch.qos.logback" % "logback-classic" % "1.3.0-alpha4",
      "org.webjars" % "swagger-ui" % "3.20.9",
      "com.softwaremill.tapir" %% "tapir-openapi-docs" % tapirVersion,
      "com.softwaremill.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion,
      "com.softwaremill.sttp" %% "async-http-client-backend-cats" % "1.5.11" % Test,
      "com.softwaremill.tapir" %% "tapir-sttp-client" % tapirVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.5" % Test,
      "com.softwaremill.tapir" %% "tapir-swagger-ui-akka-http" % "0.11.1",
      "com.softwaremill.tapir" %% "tapir-swagger-ui-http4s" % "0.11.1",
      "com.softwaremill.sttp" %% "core" % "1.6.7",
      "org.tpolecat" %% "doobie-core"     % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-specs2"   % doobieVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings",
)
