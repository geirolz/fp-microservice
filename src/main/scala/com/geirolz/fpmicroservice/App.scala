package com.geirolz.fpmicroservice

import cats.effect.{IO, IOApp, Resource, ResourceIO}
import com.geirolz.fpmicroservice.App.logger
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.SelfAwareStructuredLogger

object App extends IOApp.Simple {

  import cats.implicits.*
  import pureconfig.*

  private val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  override def run: IO[Unit] =
    (
      for {
        // ----------------- CONFIGURATION -------------------
        _      <- logger.info("Loading configuration...").to[ResourceIO]
        config <- loadConfiguration
        _      <- logger.info(config.show).to[ResourceIO]
        _      <- logger.info("Configuration successfully loaded.").to[ResourceIO]

        // -------------------- SERVICES ----------------------
        _        <- logger.info("Building services environment...").to[ResourceIO]
        services <- AppServices.make(config)
        _        <- logger.info("Services environment successfully built.").to[ResourceIO]

        // ---------------------- APP -------------------------
        _ <- logger.info("Building app...").to[ResourceIO]
        resources = AppResources.make(config, services)
        _ <- logger.info("App successfully built.").to[ResourceIO]
      } yield resources
    ).use { resources =>
      for {
        _ <- logger.info("Starting application...")
        _ <- resources.use_
        _ <- logger.info("Starting application...")
      } yield ()
    }

  private def loadConfiguration: ResourceIO[AppConfig] =
    Resource.eval {
      ConfigSource.default.load[AppConfig] match {
        case Left(failures) => IO.raiseError(new RuntimeException(failures.prettyPrint()))
        case Right(config)  => IO.pure(config)
      }
    }
}
