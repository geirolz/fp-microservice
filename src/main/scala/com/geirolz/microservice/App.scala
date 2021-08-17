package com.geirolz.microservice

import cats.effect.{IO, IOApp, Resource}
import com.comcast.ip4s.{Hostname, Port}
import com.geirolz.microservice.infra.config.Config
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object App extends IOApp.Simple {

  import cats.implicits._
  import pureconfig._
  import pureconfig.generic.auto._

  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  override def run: IO[Unit] =
    (for {
      _ <- logger.info("Starting application...")

      //---------------- CONFIGURATION ----------------
      _      <- logger.info("Loading configuration...")
      config <- loadConfiguration
      _      <- logger.info(config.show)
      _      <- logger.info("Configuration successfully loaded.")

      //-------------------- ENV ----------------------
      _   <- logger.info("Building environment...")
      env <- Env.load(config)
      _   <- logger.info("Environment successfully built.")

      //------------------- SERVER --------------------
      _ <- logger.info("Building app server...")
      server = buildServer(config, env)
    } yield server).flatMap(_.useForever)

  private def loadConfiguration: IO[Config] =
    ConfigSource.default.load[Config] match {
      case Left(failures) => IO.raiseError(new RuntimeException(failures.prettyPrint()))
      case Right(config)  => IO.pure(config)
    }

  private def buildServer(config: Config, env: Env): Resource[IO, Server] =
    EmberServerBuilder
      .default[IO]
      .withHost(Hostname.fromString(config.http.server.host).get)
      .withPort(Port.fromInt(config.http.server.port).get)
      .withHttpApp(Routes.makeApp(config, env))
      .build
}
