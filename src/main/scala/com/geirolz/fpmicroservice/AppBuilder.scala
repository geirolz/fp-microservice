package com.geirolz.fpmicroservice

import cats.Show
import cats.effect.{IO, ResourceIO}
import org.typelevel.log4cats.StructuredLogger

object AppBuilder {

  import cats.implicits.*

  def build[CONFIG: Show, SERVICES](appName: String)(
    logger: StructuredLogger[IO],
    configLoader: IO[CONFIG],
    servicesBuilder: CONFIG => ResourceIO[SERVICES],
    appResourcesBuilder: (CONFIG, SERVICES) => IO[List[ResourceIO[Unit]]]
  ): IO[Unit] = {

    val appResources: ResourceIO[List[ResourceIO[Unit]]] =
      for {
        // ------------------- CONFIGURATION ------------------
        _      <- logger.info("Loading configuration...").to[ResourceIO]
        config <- configLoader.to[ResourceIO]
        _      <- logger.info("Configuration successfully loaded.").to[ResourceIO]
        _      <- logger.info(config.show).to[ResourceIO]

        // ------------------ --- SERVICES --------------------
        _        <- logger.info("Building services environment...").to[ResourceIO]
        services <- servicesBuilder(config)
        _        <- logger.info("Services environment successfully built.").to[ResourceIO]

        // --------------------- RESOURCES --------------------
        _   <- logger.info("Building App...").to[ResourceIO]
        app <- appResourcesBuilder(config, services).to[ResourceIO]
        _   <- logger.info("App successfully built.").to[ResourceIO]
      } yield app

    logger.info(s"Starting $appName...") >>
    appResources
      .onFinalize(logger.info(s"Shutting down $appName..."))
      .use(_.parTraverse(_.useForever))
      .onCancel(logger.info(s"$appName was stopped."))
      .onError(_ => logger.info(s"$appName was stopped due an error."))
      .void
  }
}
