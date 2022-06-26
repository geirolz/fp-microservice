package com.geirolz.fpmicroservice

import cats.effect.{IO, Resource, ResourceIO}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server

object AppResources {

  import cats.implicits.*
  import cats.effect.implicits.*

  def make(config: AppConfig, env: AppServices): ResourceIO[Nothing] = {
    Resource.eval(
      List(
        buildServer(config, env).useForever
      ).parSequence >> IO.never
    )
  }

  private def buildServer(config: AppConfig, env: AppServices): ResourceIO[Server] =
    EmberServerBuilder
      .default[IO]
      .withHost(config.http.server.host)
      .withPort(config.http.server.port)
      .withHttpApp(AppRoutes.makeApp(config, env))
      .build
}
