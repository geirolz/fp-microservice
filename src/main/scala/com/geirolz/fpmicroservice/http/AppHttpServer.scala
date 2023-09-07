package com.geirolz.fpmicroservice.http

import cats.effect.IO
import com.geirolz.fpmicroservice.http.endpoint.docs.DocsEndpoints
import com.geirolz.fpmicroservice.http.endpoint.infra.InfraEndpoints
import com.geirolz.fpmicroservice.http.endpoint.user.UserEndpoints
import com.geirolz.fpmicroservice.{AppConfig, AppDependentServices, AppInfo}
import org.http4s.HttpApp
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import sttp.tapir.server.interceptor.decodefailure.DefaultDecodeFailureHandler
import sttp.tapir.server.interceptor.exception.DefaultExceptionHandler
import sttp.tapir.server.interceptor.reject.DefaultRejectHandler
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics

import scala.annotation.unused

object AppHttpServer {

  val metrics: PrometheusMetrics[IO] =
    PrometheusMetrics.default[IO]()

  private val defaultServerOptions: Http4sServerOptions[IO] =
    Http4sServerOptions
      .customiseInterceptors[IO]
      .rejectHandler(DefaultRejectHandler[IO])
      .decodeFailureHandler(DefaultDecodeFailureHandler.default)
      .exceptionHandler(DefaultExceptionHandler[IO])
      .metricsInterceptor(metrics.metricsInterceptor())
      .serverLog(Http4sServerOptions.defaultServerLog[IO])
      .options

  def make(
    info: AppInfo,
    @unused config: AppConfig,
    env: AppDependentServices,
    serverOptions: Http4sServerOptions[IO] = AppHttpServer.defaultServerOptions
  ): HttpApp[IO] = {

    val allServerEndpoints: List[ServerEndpoint[Any, IO]] = List(
      InfraEndpoints.make(info, metrics).serverEndpoints,
      UserEndpoints.make(env.userService).serverEndpoints
    ).flatten

    val docServerEndpoints: List[ServerEndpoint[Any, IO]] =
      DocsEndpoints
        .fromServerEndpoints(info, allServerEndpoints)
        .serverEndpoints

    Http4sServerInterpreter[IO](serverOptions)
      .toRoutes(allServerEndpoints ++ docServerEndpoints)
      .orNotFound
  }
}
