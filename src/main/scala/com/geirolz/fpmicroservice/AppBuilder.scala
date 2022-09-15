package com.geirolz.fpmicroservice

import cats.Show
import cats.effect.{IO, ResourceIO}
import com.geirolz.fpmicroservice.model.AppInfo
import org.typelevel.log4cats.StructuredLogger

object AppBuilder {

  import cats.implicits.*

  def build[CONFIG: Show, SERVICES](appInfo: AppInfo)(
    logger: StructuredLogger[IO],
    configLoader: IO[CONFIG],
    dependencyServicesBuilder: CONFIG => ResourceIO[SERVICES],
    providedServicesBuilder: (CONFIG, SERVICES) => IO[List[ResourceIO[Unit]]]
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
        services <- dependencyServicesBuilder(config)
        _        <- logger.info("Services environment successfully built.").to[ResourceIO]

        // --------------------- RESOURCES --------------------
        _   <- logger.info("Building App...").to[ResourceIO]
        app <- providedServicesBuilder(config, services).to[ResourceIO]
        _   <- logger.info("App successfully built.").to[ResourceIO]
      } yield app

    logger.info(s"Starting ${appInfo.buildRefName}...") >>
    appResources
      .onFinalize(logger.info(s"Shutting down ${appInfo.name}..."))
      .use(_.parTraverse(_.useForever))
      .onCancel(logger.info(s"${appInfo.name} was stopped."))
      .onError(_ => logger.info(s"${appInfo.name} was stopped due an error."))
      .void
  }
}
