import sbt._

object ProjectDependencies {

  import Organizations._
  import Versions._

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
    db
  ).flatten

  private val general = Seq(
    `com.github.geirolz` %% "scope-core"                    % scopeVersion,
    `com.github.geirolz` %% "app-toolkit-core"              % appToolkitVersion,
    `com.github.geirolz` %% "app-toolkit-config"            % appToolkitVersion,
    `com.github.geirolz` %% "app-toolkit-config-pureconfig" % appToolkitVersion,
    `com.github.geirolz` %% "app-toolkit-log4cats"          % appToolkitVersion,
    `eu.timepit`         %% "refined"                       % refinedVersion,
    `eu.timepit`         %% "refined-cats"                  % refinedVersion,
    `eu.timepit`         %% "refined-pureconfig"            % refinedVersion
  )

  private val effects =
    Seq(
      `org.typelevel` %% "cats-core"   % catsVersion,
      `org.typelevel` %% "cats-effect" % catsEffectVersion
    )

  private val config = {
    Seq(
      `com.github.pureconfig` %% "pureconfig-core"    % pureConfigVersion,
      `com.github.pureconfig` %% "pureconfig-generic" % pureConfigVersion,
      `com.github.pureconfig` %% "pureconfig-http4s"  % pureConfigVersion
    )
  }

  private val http =
    Seq(
      // HTTP
      `org.http4s` %% "http4s-dsl"          % http4sVersion,
      `org.http4s` %% "http4s-circe"        % http4sVersion,
      `org.http4s` %% "http4s-ember-server" % http4sVersion,
      // TAPIR
      `com.softwaremill.sttp.tapir` %% "tapir-core"          % tapirVersion,
      `com.softwaremill.sttp.tapir` %% "tapir-http4s-server" % tapirVersion,
      `com.softwaremill.sttp.tapir` %% "tapir-json-circe"    % tapirVersion,
      `com.softwaremill.sttp.tapir` %% "tapir-openapi-docs"  % tapirVersion,
      `com.softwaremill.sttp.tapir` %% "tapir-swagger-ui"    % tapirVersion,
      `com.softwaremill.sttp.tapir` %% "tapir-cats"          % tapirVersion,
      `com.softwaremill.sttp.tapir` %% "tapir-refined"       % tapirVersion,

      // Open Api YAML
      `com.softwaremill.sttp.apispec` %% "openapi-circe-yaml" % apiSpecOpenapiCirce
    )

  private val json = Seq(
    `io.circe` %% "circe-core"           % circeVersion,
    `io.circe` %% "circe-refined"        % circeVersion,
    `io.circe` %% "circe-generic-extras" % circeGenericExVersion
  )

  private val metrics =
    Seq(
      `com.softwaremill.sttp.tapir` %% "tapir-prometheus-metrics" % tapirVersion
    )

  private val logging = Seq(
    `org.typelevel` %% "log4cats-slf4j" % log4catsVersion,
    `org.slf4j`      % "slf4j-api"      % slf4Version,
    `org.slf4j`      % "slf4j-simple"   % slf4Version
  )

  private val tests = Seq(
    `org.scalameta` %% "munit"               % munitVersion    % Test,
    `org.typelevel` %% "munit-cats-effect-3" % munitCE3Version % Test
  )

  // externals services
  private val db =
    Seq(
      // migrations
      `com.github.geirolz` %% "fly4s-core" % fly4sVersion,

      // management
      `org.tpolecat` %% "doobie-core"           % doobieVersion,
      `org.tpolecat` %% "doobie-hikari"         % doobieVersion,
      `org.tpolecat` %% "doobie-postgres"       % doobieVersion,
      `org.tpolecat` %% "doobie-postgres-circe" % doobieVersion,
      `org.tpolecat` %% "doobie-h2"             % doobieVersion,
      `org.tpolecat` %% "doobie-scalatest"      % doobieVersion % Test,

      // connection
      `org.postgresql` % "postgresql" % "42.6.0"
    )
}

object Versions {
  type Version = String
  val appToolkitVersion: Version     = "0.0.5"
  val scopeVersion: Version          = "0.0.7"
  val catsVersion: Version           = "2.9.0"
  val catsEffectVersion: Version     = "3.4.11"
  val http4sVersion: Version         = "0.23.18"
  val tapirVersion: Version          = "1.3.0"
  val apiSpecOpenapiCirce: Version   = "0.3.2"
  val pureConfigVersion: Version     = "0.17.2"
  val doobieVersion: Version         = "1.0.0-RC2"
  val fs2RabbitVersion: Version      = "5.0.0"
  val refinedVersion: Version        = "0.10.3"
  val circeVersion: Version          = "0.14.4"
  val circeGenericExVersion: Version = "0.14.3"
  val slf4Version: Version           = "2.0.6"
  val log4catsVersion: Version       = "2.6.0"
  val fly4sVersion: Version          = "0.0.17"
  val munitVersion: Version          = "0.7.29"
  val munitCE3Version: Version       = "1.0.7"
}

object Organizations {

  type Organization = String
  val `com.github.geirolz`: Organization            = "com.github.geirolz"
  val `eu.timepit`: Organization                    = "eu.timepit"
  val `io.circe`: Organization                      = "io.circe"
  val `com.github.pureconfig`: Organization         = "com.github.pureconfig"
  val `com.softwaremill.sttp`: Organization         = "com.softwaremill.sttp"
  val `com.softwaremill.sttp.tapir`: Organization   = s"${`com.softwaremill.sttp`}.tapir"
  val `com.softwaremill.sttp.apispec`: Organization = s"${`com.softwaremill.sttp`}.apispec"

  // org
  val `org.typelevel`: Organization  = "org.typelevel"
  val `org.tpolecat`: Organization   = "org.tpolecat"
  val `org.postgresql`: Organization = "org.postgresql"
  val `org.scalameta`: Organization  = "org.scalameta"
  val `org.slf4j`: Organization      = "org.slf4j"
  val `org.http4s`: Organization     = "org.http4s"
}
