import sbt._

object ProjectDependencies {

  lazy val common: Seq[ModuleID] = Seq(
    effects,
    config,
    http,
    json,
    logging,
    tests,
    //externals services
    db,
    rabbitMq
  ).flatten

  private val effects = {
    Seq(
      "org.typelevel" %% "cats-core" % "2.6.1",
      "org.typelevel" %% "cats-effect" % "3.2.9"
    )
  }

  private val config = Seq(
    "com.github.pureconfig" %% "pureconfig" % "0.16.0"
  )

  private val http = {
    val http4sVersion = "0.23.3"
    val tapirVersion = "0.19.0-M4"

    Seq(
      //HTTP
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      //TAPIR
      "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-cats" % tapirVersion
    )
  }

  private val json = Seq(
    "io.circe" %% "circe-core" % "0.14.1",
    "io.circe" %% "circe-generic-extras" % "0.14.1"
  )

  private val logging = Seq(
    "org.typelevel" %% "log4cats-slf4j" % "2.1.1",
    "org.slf4j" % "slf4j-api" % "1.7.32",
    "org.slf4j" % "slf4j-simple" % "1.7.32"
  )

  private val tests = Seq(
    "org.scalactic" %% "scalactic" % "3.2.10",
    "org.scalatest" %% "scalatest" % "3.2.9" % Test
  )

  //externals services
  private val db = {
    val doobieVersion = "1.0.0-RC1"
    Seq(
      "com.github.geirolz" %% "fly4s-core" % "0.0.2",
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "org.tpolecat" %% "doobie-h2" % doobieVersion,
      "org.tpolecat" %% "doobie-scalatest" % doobieVersion % Test
    )
  }

  private val rabbitMq = {
    val fs2RabbitVersion = "4.1.0"
    Seq(
      "dev.profunktor" %% "fs2-rabbit" % fs2RabbitVersion,
      "dev.profunktor" %% "fs2-rabbit-circe" % fs2RabbitVersion
    )
  }
}
