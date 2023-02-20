package com.geirolz.fpmicroservice

import cats.effect.{IO, IOApp}
import com.geirolz.app.toolkit.{AppBuilder, AppResources}
import com.geirolz.app.toolkit.config.pureconfig.syntax.*
import com.geirolz.app.toolkit.logger.LoggerAdapter
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.SelfAwareStructuredLogger

object App extends IOApp.Simple {

  // TODO: To remove with the next version of app-toolkit 0.0.5
  implicit private val loggerAdapter: LoggerAdapter[SelfAwareStructuredLogger] =
    implicitly[LoggerAdapter[SelfAwareStructuredLogger]]

  override def run: IO[Unit] =
    AppBuilder[IO]
      .withResourcesLoader(
        AppResources
          .loader[IO, AppInfo](AppInfo.fromBuildInfo)
          .withLogger(Slf4jLogger.getLogger[IO])
          .withPureConfigLoader[AppConfig]
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
