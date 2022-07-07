package com.geirolz.fpmicroservice.http.route

import cats.effect.IO
import com.geirolz.fpmicroservice.http.route.endpoint.infra.InfraEndpoints
import com.geirolz.fpmicroservice.http.route.endpoint.infra.contract.{
  AppInfoContract,
  AppMetricsReportContract
}
import com.geirolz.fpmicroservice.model.{AppInfo, AppMetricsReport}
import scope.{InScope, Scope}
import sttp.tapir.server.http4s.Http4sServerInterpreter

class InfraRoutes private extends InScope[Scope.Endpoint] {

  import cats.implicits.*
  import scope.syntax.*
  import ServerRoutes.*

  private val interpreter: Http4sServerInterpreter[IO] =
    Http4sServerInterpreter[IO](AppRoutes.defaultServerOptions)

  private val healthcheckRoute: Http4sServerRoutes[IO] =
    InfraEndpoints.healthcheck
      .serverLogic(_.asRight[Unit].pure[IO])
      .toRoutes(interpreter)

  private val appInfoRoute: Http4sServerRoutes[IO] =
    InfraEndpoints.getAppInfo
      .serverLogic(_ => AppInfo.value.scoped.as[AppInfoContract].asRight[Unit].pure[IO])
      .toRoutes(interpreter)

  private val appMetricsRoute: Http4sServerRoutes[IO] =
    InfraEndpoints.getAppMetrics
      .serverLogic(_ =>
        AppMetricsReport.fromCurrentRuntime.map(
          _.scoped.as[AppMetricsReportContract].asRight[Unit]
        )
      )
      .toRoutes(interpreter)

  val routes: Http4sServerRoutes[IO] =
    healthcheckRoute <+> appInfoRoute <+> appMetricsRoute
}
object InfraRoutes {
  def make: InfraRoutes = new InfraRoutes
}
