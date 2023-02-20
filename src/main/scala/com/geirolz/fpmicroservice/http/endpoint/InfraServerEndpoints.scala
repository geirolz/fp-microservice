package com.geirolz.fpmicroservice.http.endpoint

import cats.effect.IO
import com.geirolz.fpmicroservice.http.endpoint.api.infra.InfraEndpoints
import com.geirolz.fpmicroservice.http.endpoint.api.infra.contract.AppInfoResponse
import com.geirolz.fpmicroservice.{App, AppInfo}
import scope.{InScope, Scope}
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics

private[http] class InfraServerEndpoints private (info: AppInfo, metrics: PrometheusMetrics[IO])
    extends InScope[Scope.Endpoint] {

  import cats.implicits.*
  import scope.syntax.*

  private val healthcheckRoute: ServerEndpoint[Any, IO] =
    InfraEndpoints.healthcheck
      .serverLogic(_.asRight[Unit].pure[IO])

  private val appInfoRoute: ServerEndpoint[Any, IO] =
    InfraEndpoints.getAppInfo
      .serverLogic(_ => info.scoped.as[AppInfoResponse].asRight[Unit].pure[IO])

  val serverEndpoints: List[ServerEndpoint[Any, IO]] =
    List(
      healthcheckRoute,
      appInfoRoute,
      metrics.metricsEndpoint
    )
}
private[http] object InfraServerEndpoints {
  def make(info: AppInfo, metrics: PrometheusMetrics[IO]): InfraServerEndpoints =
    new InfraServerEndpoints(info, metrics)
}
