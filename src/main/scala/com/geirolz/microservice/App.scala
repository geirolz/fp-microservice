package com.geirolz.microservice

import cats.effect.{IO, IOApp, Resource}
import com.comcast.ip4s.{Hostname, Port}
import com.geirolz.microservice.common.logging.Logging
import com.geirolz.microservice.Config
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server

object App extends IOApp.Simple with Logging.IOLog with Logging.IOResourceLog {

  import cats.implicits.*
  import pureconfig.*

  override def run: IO[Unit] =
    (
      for {
        // ---------------- CONFIGURATION ----------------
        _      <- resourceLogger.info("Loading configuration...")
        config <- loadConfiguration
        _      <- resourceLogger.info(config.show)
        _      <- resourceLogger.info("Configuration successfully loaded.")

        // -------------------- ENV ----------------------
        _   <- resourceLogger.info("Building environment...")
        env <- Env.make(config)
        _   <- resourceLogger.info("Environment successfully built.")

        // -------------------- SERVER ----------------------
        _ <- resourceLogger.info("Building server...")
        server = buildServer(config, env)
        _ <- resourceLogger.info("Server successfully built.")
      } yield server
    ).use { server =>
      logger.info("Starting application...") >> server.useForever
    }

  private def loadConfiguration: Resource[IO, Config] =
    Resource.eval {
      ConfigSource.default.load[Config] match {
        case Left(failures) => IO.raiseError(new RuntimeException(failures.prettyPrint()))
        case Right(config)  => IO.pure(config)
      }
    }

  private def buildServer(config: Config, env: Env): Resource[IO, Server] =
    EmberServerBuilder
      .default[IO]
      .withHost(config.http.server.host)
      .withPort(config.http.server.port)
      .withHttpApp(Routes.makeApp(config, env))
      .build
}
