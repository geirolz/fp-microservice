package com.geirolz.fpmicroservice.http.route

import cats.effect.IO
import cats.implicits.toSemigroupKOps
import com.geirolz.fpmicroservice.{AppConfig, AppServices}
import com.geirolz.fpmicroservice.http.route.ServerRoutes.Http4sServerRoutes
import org.http4s.{HttpApp, HttpRoutes}
import sttp.tapir.server.http4s.Http4sServerOptions
import sttp.tapir.server.interceptor.decodefailure.DefaultDecodeFailureHandler
import sttp.tapir.server.interceptor.exception.DefaultExceptionHandler
import sttp.tapir.server.interceptor.reject.DefaultRejectHandler

import scala.annotation.unused

class AppRoutes private (
  @unused config: AppConfig,
  env: AppServices
) {

  val routes: HttpRoutes[IO] = {

    val allRoutes: Http4sServerRoutes[IO] = List(
      InfraRoutes.make.routes,
      UserRoutes.make(env.userService).routes
    ).reduce(_ <+> _)

    val docRoutes: DocsRoutes = DocsRoutes
      .fromServerEndpoints(allRoutes.endpoints)

    docRoutes.routes <+> allRoutes.interpretedRoutes
  }
}
object AppRoutes {

  val defaultServerOptions: Http4sServerOptions[IO] =
    Http4sServerOptions
      .customiseInterceptors[IO]
      .rejectHandler(DefaultRejectHandler[IO])
      .decodeFailureHandler(DefaultDecodeFailureHandler.default)
      .exceptionHandler(DefaultExceptionHandler[IO])
      .serverLog(Http4sServerOptions.defaultServerLog[IO])
      .options

  def makeApp(config: AppConfig, env: AppServices): HttpApp[IO] =
    new AppRoutes(config, env).routes.orNotFound
}
