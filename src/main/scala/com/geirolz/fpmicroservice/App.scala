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
  private val appName: String                       = BuildInfo.name

  override def run: IO[Unit] =
    app
      .use(appResources => logger.info(s"Starting $appName...") >> appResources.use_)
      .onCancel(logger.info(s"Shutting down $appName..."))
      .onError(_ => logger.info(s"Shutting down $appName due an error..."))

  private val app: Resource[IO, ResourceIO[Nothing]] =
    for {
      // ----------------- CONFIGURATION -------------------
      _      <- logger.info("Loading configuration...").to[ResourceIO]
      config <- IO(ConfigSource.default.loadOrThrow[AppConfig]).to[ResourceIO]
      _      <- logger.info(config.show).to[ResourceIO]
      _      <- logger.info("Configuration successfully loaded.").to[ResourceIO]

      // -------------------- SERVICES ----------------------
      _        <- logger.info("Building services environment...").to[ResourceIO]
      services <- AppServices.make(config)
      _        <- logger.info("Services environment successfully built.").to[ResourceIO]

      // ---------------------- APP -------------------------
      _ <- logger.info("Building resources...").to[ResourceIO]
      resources = AppResources.make(config, services)
      _ <- logger.info("Resources successfully built.").to[ResourceIO]
    } yield resources

}
