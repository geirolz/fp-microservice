import sbt._

object ProjectDependencies {

  val http4sVersion     = "0.23.14"
  val tapirVersion      = "1.0.3"
  val pureConfigVersion = "0.17.1"
  val doobieVersion     = "1.0.0-RC2"
  val fs2RabbitVersion  = "5.0.0"

  lazy val common: Seq[ModuleID] = Seq(
    general,
    effects,
    config,
    http,
    json,
    metrics,
    logging,
    tests,
    // externals services
    db,
    rabbitMq
  ).flatten

  private val general = Seq(
    "com.github.geirolz" %% "scope-core" % "0.0.5",
    "eu.timepit" %% "refined" % "0.10.1",
    "eu.timepit" %% "refined-cats" % "0.10.1",
    "eu.timepit" %% "refined-pureconfig" % "0.10.1"
  )

  private val effects =
    Seq(
      "org.typelevel" %% "cats-core" % "2.8.0",
      "org.typelevel" %% "cats-effect" % "3.3.14"
    )

  private val config = {
    Seq(
      "com.github.pureconfig" %% "pureconfig-core" % pureConfigVersion,
      "com.github.pureconfig" %% "pureconfig-generic" % pureConfigVersion,
      "com.github.pureconfig" %% "pureconfig-http4s" % pureConfigVersion
    )
  }

  private val http =
    Seq(
      // HTTP
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      // TAPIR
      "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-cats" % tapirVersion,

      // Open Api YAML
      "com.softwaremill.sttp.apispec" %% "openapi-circe-yaml" % "0.2.1"
    )

  private val json = Seq(
    "io.circe" %% "circe-core" % "0.14.2",
    "io.circe" %% "circe-generic-extras" % "0.14.2",
    "io.circe" %% "circe-refined" % "0.14.2"
  )

  private val metrics =
    Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % tapirVersion
    )

  private val logging = Seq(
    "org.typelevel" %% "log4cats-slf4j" % "2.4.0",
    "org.slf4j" % "slf4j-api" % "1.7.36",
    "org.slf4j" % "slf4j-simple" % "1.7.36"
  )

  private val tests = Seq(
    "org.scalameta" %% "munit" % "0.7.29" % Test,
    "org.typelevel" %% "munit-cats-effect-3" % "1.0.7" % Test
  )

  // externals services
  private val db =
    Seq(
      // migrations
      "com.github.geirolz" %% "fly4s-core" % "0.0.13",

      // management
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-postgres-circe" % doobieVersion,
      "org.tpolecat" %% "doobie-h2" % doobieVersion,
      "org.tpolecat" %% "doobie-scalatest" % doobieVersion % Test,

      // connection
      "org.postgresql" % "postgresql" % "42.4.0"
    )

  private val rabbitMq = Seq(
    "dev.profunktor" %% "fs2-rabbit" % fs2RabbitVersion,
    "dev.profunktor" %% "fs2-rabbit-circe" % fs2RabbitVersion
  )
}
