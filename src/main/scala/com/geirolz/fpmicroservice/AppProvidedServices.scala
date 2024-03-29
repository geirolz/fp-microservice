package com.geirolz.fpmicroservice

import cats.effect.{IO, ResourceIO}
import com.geirolz.app.toolkit.App
import com.geirolz.app.toolkit.novalues.NoResources
import com.geirolz.fpmicroservice.http.AppHttpServer
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.typelevel.log4cats.SelfAwareStructuredLogger

object AppProvidedServices {

  def fromAppDependencies(
    deps: App.Dependencies[
      AppInfo,
      SelfAwareStructuredLogger[IO],
      AppConfig,
      AppDependentServices,
      NoResources
    ]
  ): List[IO[Any]] =
    List(
      httpServerResource(deps.info, deps.config, deps.dependencies).useForever
    )

  private def httpServerResource(
    info: AppInfo,
    config: AppConfig,
    dependencies: AppDependentServices
  ): ResourceIO[Server] =
    EmberServerBuilder
      .default[IO]
      .withHost(config.http.server.host)
      .withPort(config.http.server.port)
      .withHttpApp(AppHttpServer.make(info, config, dependencies))
      .build
}
