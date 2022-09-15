package com.geirolz.fpmicroservice

import cats.effect.{IO, IOApp, Resource, ResourceIO}
import cats.Show
import com.geirolz.fpmicroservice.model.AppInfo
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.{SelfAwareStructuredLogger, StructuredLogger}

object App extends IOApp.Simple {

  import pureconfig.*

  override def run: IO[Unit] =
    AppBuilder.build(AppInfo.value)(
      logger                    = Slf4jLogger.getLogger[IO],
      configLoader              = IO(ConfigSource.default.loadOrThrow[AppConfig]),
      dependencyServicesBuilder = config => AppDependencyServices.make(config),
      providedServicesBuilder   = (config, services) => AppProvidedServices.make(config, services)
    )

}
