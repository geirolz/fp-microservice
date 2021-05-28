import sbt._

object ProjectDependencies {

  lazy val common: Seq[ModuleID] = Seq(
    //SCALA
    "org.typelevel" %% "cats-core" % "2.5.0",
    "org.typelevel" %% "cats-effect" % "2.5.0",
    //HTTP
    "org.http4s" %% "http4s-dsl" % "0.21.22",
    "org.http4s" %% "http4s-blaze-server" % "0.21.22",
    //TAPIR
    "com.softwaremill.sttp.tapir" %% "tapir-core" % "0.17.19",
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "0.17.19",
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "0.17.19",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % "0.17.19",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % "0.17.19",
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s" % "0.17.19",
    //JSON
    "io.circe" %% "circe-core" % "0.13.0",
    //CONFIG
    "com.github.pureconfig" %% "pureconfig" % "0.15.0",
    //LOGGING
    "org.typelevel" %% "log4cats-slf4j" % "1.3.0",
    "org.slf4j" % "slf4j-api" % "1.7.30",
    "org.slf4j" % "slf4j-simple" % "1.7.30"
  )
}
