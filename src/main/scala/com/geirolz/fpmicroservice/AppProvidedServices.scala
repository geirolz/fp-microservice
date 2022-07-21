package com.geirolz.fpmicroservice

import cats.effect.{IO, ResourceIO}
import com.geirolz.fpmicroservice.http.HttpServerApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server

object AppProvidedServices {

  import cats.implicits.*

  def make(config: AppConfig, env: AppDependencyServices): IO[List[ResourceIO[Unit]]] =
    List(
      buildServer(config, env)
    ).map(_.void).pure[IO]

  private def buildServer(config: AppConfig, env: AppDependencyServices): ResourceIO[Server] = {
    EmberServerBuilder
      .default[IO]
      .withHost(config.http.server.host)
      .withPort(config.http.server.port)
      .withHttpApp(HttpServerApp.make(config, env))
      .build
  }
}
