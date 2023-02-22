package com.geirolz.fpmicroservice

import cats.effect.{IO, ResourceIO}
import com.geirolz.app.toolkit.AppDependencies
import com.geirolz.fpmicroservice.http.AppHttpServer
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.typelevel.log4cats.SelfAwareStructuredLogger

object AppProvidedServices {

  import cats.implicits.*

  def fromAppDependencies(
    deps: AppDependencies[AppInfo, SelfAwareStructuredLogger[IO], AppConfig, AppDependentServices]
  ): IO[List[IO[Any]]] =
    List(
      httpServerResource(deps.info, deps.config, deps.dependencies).useForever
    ).map(_.void).pure[IO]

  private def httpServerResource(
    info: AppInfo,
    config: AppConfig,
    dependencies: AppDependentServices
  ): ResourceIO[Server] = {
    EmberServerBuilder
      .default[IO]
      .withHost(config.http.server.host)
      .withPort(config.http.server.port)
      .withHttpApp(AppHttpServer.make(info, config, dependencies))
      .build
  }
}
