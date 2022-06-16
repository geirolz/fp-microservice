package com.geirolz.fpmicroservice.route

import cats.effect.IO
import com.geirolz.fpmicroservice.model.{AppInfo, AppMetricsReport}
import com.geirolz.fpmicroservice.route.endpoint.infra.InfraEndpointsApi
import com.geirolz.fpmicroservice.route.endpoint.infra.contract.{
  AppInfoContract,
  AppMetricsReportContract
}
import com.geirolz.fpmicroservice.AppRoutes
import org.http4s.HttpRoutes
import scope.{InScope, Scope}
import sttp.tapir.server.http4s.Http4sServerInterpreter

class InfraRoutes private extends InScope[Scope.Endpoint] {

  import cats.implicits.*
  import scope.syntax.*

  private val interpreter: Http4sServerInterpreter[IO] =
    Http4sServerInterpreter[IO](AppRoutes.defaultServerOptions)

  private val appInfoRoute =
    interpreter.toRoutes(
      InfraEndpointsApi.getAppInfo
        .serverLogic(_ => AppInfo.value.scoped.as[AppInfoContract].asRight[Unit].pure[IO])
    )

  private val appMetricsRoute =
    interpreter.toRoutes(
      InfraEndpointsApi.getAppMetrics
        .serverLogic(_ =>
          AppMetricsReport.fromCurrentRuntime.map(
            _.scoped.as[AppMetricsReportContract].asRight[Unit]
          )
        )
    )

  val routes: HttpRoutes[IO] =
    appInfoRoute <+> appMetricsRoute
}
object InfraRoutes {
  def make: InfraRoutes = new InfraRoutes
}
