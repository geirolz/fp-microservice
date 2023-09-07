package com.geirolz.fpmicroservice.http.endpoint.infra

import cats.effect.IO
import com.geirolz.fpmicroservice.AppInfo
import com.geirolz.fpmicroservice.http.endpoint.infra.contract.AppInfoResponse
import scope.{InScope, Scope}
import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics

private[http] class InfraEndpoints private (info: AppInfo, metrics: PrometheusMetrics[IO])
    extends InScope[Scope.Endpoint] {

  import cats.implicits.*
  import scope.syntax.*

  private val healthcheckRoute: ServerEndpoint[Any, IO] =
    InfraEndpoints.Def.healthcheck
      .serverLogic(_.asRight[Unit].pure[IO])

  private val appInfoRoute: ServerEndpoint[Any, IO] =
    InfraEndpoints.Def.getAppInfo
      .serverLogic(_ => info.scoped.as[AppInfoResponse].asRight[Unit].pure[IO])

  val serverEndpoints: List[ServerEndpoint[Any, IO]] =
    List(
      healthcheckRoute,
      appInfoRoute,
      metrics.metricsEndpoint
    )
}
private[http] object InfraEndpoints {

  def make(info: AppInfo, metrics: PrometheusMetrics[IO]): InfraEndpoints =
    new InfraEndpoints(info, metrics)

  object Def {

    import sttp.tapir.generic.auto.*
    import sttp.tapir.json.circe.*

    val healthcheck: PublicEndpoint[Unit, Unit, Unit, Any] =
      endpoint.get.in("healthcheck")

    val getAppInfo: PublicEndpoint[Unit, Unit, AppInfoResponse, Any] =
      endpoint.get
        .in("info")
        .out(jsonBody[AppInfoResponse])
  }
}
