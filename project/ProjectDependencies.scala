import sbt._

object ProjectDependencies {

  lazy val common: Seq[ModuleID] = Seq(
    effects,
    logging,
    config,
    db,
    http,
    json
  ).flatten

  private val effects = {
    Seq(
      "org.typelevel" %% "cats-core" % "2.6.1",
      "org.typelevel" %% "cats-effect" % "2.5.0"
    )
  }

  private val logging = Seq(
    "org.typelevel" %% "log4cats-slf4j" % "1.3.1",
    "org.slf4j" % "slf4j-api" % "1.7.31",
    "org.slf4j" % "slf4j-simple" % "1.7.31"
  )

  private val config = Seq(
    "com.github.pureconfig" %% "pureconfig" % "0.16.0"
  )

  private val db = {
    val doobieVersion = "0.13.4"
    Seq(
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-h2" % doobieVersion,
      "org.tpolecat" %% "doobie-scalatest" % doobieVersion % "test",
      "org.flywaydb" % "flyway-core" % "7.10.0"
    )
  }

  private val http = {
    val http4sVersion = "0.21.24"
    val tapirVersion = "0.17.19"

    Seq(
      //HTTP
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      //TAPIR
      "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s" % tapirVersion
    )
  }

  private val json = Seq(
    "io.circe" %% "circe-core" % "0.14.1"
  )
}
