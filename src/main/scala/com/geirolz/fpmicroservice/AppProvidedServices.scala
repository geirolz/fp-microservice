package com.geirolz.fpmicroservice

import cats.effect.{IO, ResourceIO}
import com.geirolz.fpmicroservice.http.AppHttpServer
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server

object AppProvidedServices {

  import cats.implicits.*

  def build(
    info: AppInfo,
    config: AppConfig,
    dependencies: AppDependencyServices
  ): IO[List[IO[Any]]] =
    List(
      httpServerResource(info, config, dependencies).useForever
    ).map(_.void).pure[IO]

  private def httpServerResource(
    info: AppInfo,
    config: AppConfig,
    dependencies: AppDependencyServices
  ): ResourceIO[Server] = {
    EmberServerBuilder
      .default[IO]
      .withHost(config.http.server.host)
      .withPort(config.http.server.port)
      .withHttpApp(AppHttpServer.make(info, config, dependencies))
      .build
  }
}
