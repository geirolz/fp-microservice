package com.geirolz.fpmicroservice

import cats.effect.{IO, IOApp, Resource, ResourceIO}
import cats.Show
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.{SelfAwareStructuredLogger, StructuredLogger}

object App extends IOApp.Simple {

  import pureconfig.*

  override def run: IO[Unit] =
    AppBuilder.build(BuildInfo.name)(
      logger              = Slf4jLogger.getLogger[IO],
      configLoader        = IO(ConfigSource.default.loadOrThrow[AppConfig]),
      servicesBuilder     = config => AppServices.make(config),
      appResourcesBuilder = (config, services) => AppResources.make(config, services)
    )

}
