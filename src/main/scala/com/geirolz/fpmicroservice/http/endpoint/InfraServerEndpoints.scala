package com.geirolz.fpmicroservice.http.endpoint

import cats.effect.IO
import com.geirolz.fpmicroservice.http.endpoint.api.infra.InfraEndpoints
import com.geirolz.fpmicroservice.http.endpoint.api.infra.contract.AppInfoContract
import com.geirolz.fpmicroservice.model.AppInfo
import scope.{InScope, Scope}
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics

private[http] class InfraServerEndpoints private (metrics: PrometheusMetrics[IO])
    extends InScope[Scope.Endpoint] {

  import cats.implicits.*
  import scope.syntax.*

  private val healthcheckRoute: ServerEndpoint[Any, IO] =
    InfraEndpoints.healthcheck
      .serverLogic(_.asRight[Unit].pure[IO])

  private val appInfoRoute: ServerEndpoint[Any, IO] =
    InfraEndpoints.getAppInfo
      .serverLogic(_ => AppInfo.value.scoped.as[AppInfoContract].asRight[Unit].pure[IO])

  val serverEndpoints: List[ServerEndpoint[Any, IO]] =
    List(
      healthcheckRoute,
      appInfoRoute,
      metrics.metricsEndpoint
    )
}
private[http] object InfraServerEndpoints {
  def make(metrics: PrometheusMetrics[IO]): InfraServerEndpoints =
    new InfraServerEndpoints(metrics)
}
