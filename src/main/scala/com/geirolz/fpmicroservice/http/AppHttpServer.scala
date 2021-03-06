package com.geirolz.fpmicroservice.http

import cats.effect.IO
import com.geirolz.fpmicroservice.{AppConfig, AppDependencyServices}
import com.geirolz.fpmicroservice.http.endpoint.{
  DocsServerEndpoints,
  InfraServerEndpoints,
  UserServerEndpoints
}
import org.http4s.HttpApp
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import sttp.tapir.server.interceptor.decodefailure.DefaultDecodeFailureHandler
import sttp.tapir.server.interceptor.exception.DefaultExceptionHandler
import sttp.tapir.server.interceptor.reject.DefaultRejectHandler
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics

import scala.annotation.unused

object AppHttpServer {

  val metrics: PrometheusMetrics[IO] =
    PrometheusMetrics.default[IO]()

  val defaultServerOptions: Http4sServerOptions[IO] =
    Http4sServerOptions
      .customiseInterceptors[IO]
      .rejectHandler(DefaultRejectHandler[IO])
      .decodeFailureHandler(DefaultDecodeFailureHandler.default)
      .exceptionHandler(DefaultExceptionHandler[IO])
      .metricsInterceptor(metrics.metricsInterceptor())
      .serverLog(Http4sServerOptions.defaultServerLog[IO])
      .options

  private def make(
    interpreter: Http4sServerInterpreter[IO],
    @unused config: AppConfig,
    env: AppDependencyServices
  ): HttpApp[IO] = {

    val allServerEndpoints: List[ServerEndpoint[Any, IO]] = List(
      InfraServerEndpoints.make(metrics).serverEndpoints,
      UserServerEndpoints.make(env.userService).serverEndpoints
    ).flatten

    val docServerEndpoints: List[ServerEndpoint[Any, IO]] =
      DocsServerEndpoints
        .fromServerEndpoints(allServerEndpoints)
        .serverEndpoints

    interpreter
      .toRoutes(
        allServerEndpoints ++ docServerEndpoints
      )
      .orNotFound
  }

  def make(
    serverOptions: Http4sServerOptions[IO],
    config: AppConfig,
    env: AppDependencyServices
  ): HttpApp[IO] =
    make(
      interpreter = Http4sServerInterpreter[IO](serverOptions),
      config      = config,
      env         = env
    )

  def make(
    config: AppConfig,
    env: AppDependencyServices
  ): HttpApp[IO] =
    make(
      serverOptions = AppHttpServer.defaultServerOptions,
      config        = config,
      env           = env
    )
}
