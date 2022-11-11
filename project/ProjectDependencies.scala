import sbt._

object ProjectDependencies {

  private val catsVersion       = "2.8.0"
  private val catsEffectVersion = "3.3.14"
  private val http4sVersion     = "0.23.16"
  private val tapirVersion      = "1.1.0"
  private val pureConfigVersion = "0.17.2"
  private val doobieVersion     = "1.0.0-RC2"
  private val fs2RabbitVersion  = "5.0.0"
  private val refinedVersion    = "0.10.1"
  private val circeVersion      = "0.14.2"
  private val slf4Version       = "2.0.0"
  private val log4catsVersion   = "2.5.0"

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
    "com.github.geirolz" %% "scope-core" % "0.0.6",
    "eu.timepit" %% "refined" % refinedVersion,
    "eu.timepit" %% "refined-cats" % refinedVersion,
    "eu.timepit" %% "refined-pureconfig" % refinedVersion
  )

  private val effects =
    Seq(
      "org.typelevel" %% "cats-core" % catsVersion,
      "org.typelevel" %% "cats-effect" % catsEffectVersion
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
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic-extras" % circeVersion,
    "io.circe" %% "circe-refined" % circeVersion
  )

  private val metrics =
    Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % tapirVersion
    )

  private val logging = Seq(
    "org.typelevel" %% "log4cats-slf4j" % log4catsVersion,
    "org.slf4j" % "slf4j-api" % slf4Version,
    "org.slf4j" % "slf4j-simple" % slf4Version
  )

  private val tests = Seq(
    "org.scalameta" %% "munit" % "0.7.29" % Test,
    "org.typelevel" %% "munit-cats-effect-3" % "1.0.7" % Test
  )

  // externals services
  private val db =
    Seq(
      // migrations
      "com.github.geirolz" %% "fly4s-core" % "0.0.14",

      // management
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-postgres-circe" % doobieVersion,
      "org.tpolecat" %% "doobie-h2" % doobieVersion,
      "org.tpolecat" %% "doobie-scalatest" % doobieVersion % Test,

      // connection
      "org.postgresql" % "postgresql" % "42.4.2"
    )

  private val rabbitMq = Seq(
    "dev.profunktor" %% "fs2-rabbit" % fs2RabbitVersion,
    "dev.profunktor" %% "fs2-rabbit-circe" % fs2RabbitVersion
  )
}
