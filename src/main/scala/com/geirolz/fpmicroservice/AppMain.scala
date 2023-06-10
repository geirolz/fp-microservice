package com.geirolz.fpmicroservice

import cats.effect.{ExitCode, IO, IOApp}
import com.geirolz.app.toolkit.config.pureconfig.syntax.*
import com.geirolz.app.toolkit.App
import com.geirolz.app.toolkit.novalues.NoResources
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object AppMain extends IOApp.Simple {

  override def run: IO[Unit] =
    App[IO]
      .withInfo(AppInfo.fromBuildInfo)
      .withLogger(Slf4jLogger.getLogger[IO])
      .withPureConfigLoader[AppConfig]
      .dependsOn(AppDependentServices.fromAppResources(_))
      .provideF(AppProvidedServices.fromAppDependencies(_))
      .run_
}
