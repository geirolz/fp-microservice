package com.geirolz.fpmicroservice

import cats.effect.{IO, IOApp}
import com.geirolz.app.toolkit.App
import com.geirolz.app.toolkit.config.pureconfig.*
import com.geirolz.app.toolkit.fly4s.migrateDatabaseWithConfig
import fly4s.core.data.{Fly4sConfig, Location}
import org.typelevel.log4cats.slf4j.Slf4jLogger

object AppMain extends IOApp.Simple {

  override def run: IO[Unit] =
    App[IO]
      .withInfo(AppInfo.fromBuildInfo)
      .withLogger(Slf4jLogger.getLogger[IO])
      .withConfigLoader(pureconfigLoader[IO, AppConfig])
      .dependsOn(AppDependentServices.fromAppResources(_))
      .beforeProviding(
        migrateDatabaseWithConfig(
          url      = _.db.main.url.value,
          user     = _.db.main.username,
          password = _.db.main.password.map(_.unsafeUse.toCharArray),
          config = config =>
            Fly4sConfig.default
              .withTable(config.db.main.migrationsTable.value)
              .withLocations(config.db.main.migrationsLocations.map(ns => Location(ns.value)))
        )
      )
      .provide(AppProvidedServices.fromAppDependencies(_))
      .run_
}
