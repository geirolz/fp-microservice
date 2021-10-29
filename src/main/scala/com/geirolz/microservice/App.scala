package com.geirolz.microservice

import cats.effect.{IO, IOApp, Resource}
import com.comcast.ip4s.{Hostname, Port}
import com.geirolz.microservice.common.logging.Logging
import com.geirolz.microservice.infra.config.Config
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server

object App extends IOApp.Simple with Logging.IOLog with Logging.IOResourceLog {

  import cats.implicits._
  import pureconfig._
  import pureconfig.generic.auto._

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
        env <- Env.load(config)
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
      .withHost(Hostname.fromString(config.http.server.host).get)
      .withPort(Port.fromInt(config.http.server.port).get)
      .withHttpApp(Routes.makeApp(config, env))
      .build
}
