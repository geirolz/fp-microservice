package com.geirolz.microservice

import cats.effect.{IO, IOApp, Resource, ResourceIO}
import com.comcast.ip4s.{Hostname, Port}
import com.geirolz.microservice.Config
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.SelfAwareStructuredLogger

object App extends IOApp.Simple {

  import cats.implicits.*
  import pureconfig.*

  implicit private val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  override def run: IO[Unit] =
    (
      for {
        // ---------------- CONFIGURATION ----------------
        _      <- logger.info("Loading configuration...").to[ResourceIO]
        config <- loadConfiguration
        _      <- logger.info(config.show).to[ResourceIO]
        _      <- logger.info("Configuration successfully loaded.").to[ResourceIO]

        // -------------------- ENV ----------------------
        _   <- resourceLogger.info("Building environment...")
        env <- AppEnv.make(config)
        _   <- logger.info("Environment successfully built.").to[ResourceIO]

        // -------------------- SERVER ----------------------
        _ <- logger.info("Building server...").to[ResourceIO]
        server = buildServer(config, env)
        _ <- logger.info("Server successfully built.").to[ResourceIO]
      } yield server
    ).use { server =>
      logger.info("Starting application...") >> server.useForever
    }

  private def loadConfiguration: ResourceIO[Config] =
    Resource.eval {
      ConfigSource.default.load[Config] match {
        case Left(failures) => IO.raiseError(new RuntimeException(failures.prettyPrint()))
        case Right(config)  => IO.pure(config)
      }
    }

  private def buildServer(config: Config, env: AppEnv): ResourceIO[Server] =
    EmberServerBuilder
      .default[IO]
      .withHost(config.http.server.host)
      .withPort(config.http.server.port)
      .withHttpApp(AppRoutes.makeApp(config, env))
      .build
}
