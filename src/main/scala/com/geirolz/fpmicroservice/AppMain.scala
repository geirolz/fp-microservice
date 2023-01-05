package com.geirolz.fpmicroservice

import cats.effect.{IO, IOApp}
import com.geirolz.app.toolkit.{App as AppT, AppResources as AppTResources}
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.SelfAwareStructuredLogger

object AppMain extends IOApp.Simple {

  import pureconfig.*

  type AppResources = AppTResources[AppInfo, SelfAwareStructuredLogger[IO], AppConfig]

  override def run: IO[Unit] =
    AppT[IO]
      .withResourcesLoader(
        AppTResources
          .loader[IO, AppInfo](AppInfo.fromBuildInfo)
          .withLogger(Slf4jLogger.getLogger[IO])
          .withConfigLoader(_ => IO(ConfigSource.default.loadOrThrow[AppConfig]))
      )
      .dependsOn(AppDependencyServices.resource(_))
      .provideF(deps =>
        AppProvidedServices.build(
          info         = deps.info,
          config       = deps.config,
          dependencies = deps.dependencies
        )
      )
      .use(_.run)
}
