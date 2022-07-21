package com.geirolz.fpmicroservice.http.endpoint

import cats.effect.IO
import com.geirolz.fpmicroservice.http.endpoint.api.infra.InfraEndpoints
import com.geirolz.fpmicroservice.http.endpoint.api.infra.contract.{
  AppInfoContract,
  AppMetricsReportContract
}
import com.geirolz.fpmicroservice.model.{AppInfo, AppMetricsReport}
import scope.{InScope, Scope}
import sttp.tapir.server.ServerEndpoint

private[http] class InfraServerEndpoints private extends InScope[Scope.Endpoint] {

  import cats.implicits.*
  import scope.syntax.*

  private val healthcheckRoute: ServerEndpoint[Any, IO] =
    InfraEndpoints.healthcheck
      .serverLogic(_.asRight[Unit].pure[IO])

  private val appInfoRoute: ServerEndpoint[Any, IO] =
    InfraEndpoints.getAppInfo
      .serverLogic(_ => AppInfo.value.scoped.as[AppInfoContract].asRight[Unit].pure[IO])

  private val appMetricsRoute: ServerEndpoint[Any, IO] =
    InfraEndpoints.getAppMetrics
      .serverLogic(_ =>
        AppMetricsReport.fromCurrentRuntime.map(
          _.scoped.as[AppMetricsReportContract].asRight[Unit]
        )
      )

  val serverEndpoints: List[ServerEndpoint[Any, IO]] =
    List(
      healthcheckRoute,
      appInfoRoute,
      appMetricsRoute
    )
}
private[http] object InfraServerEndpoints {
  def make: InfraServerEndpoints = new InfraServerEndpoints
}
