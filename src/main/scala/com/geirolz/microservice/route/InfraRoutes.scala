package com.geirolz.microservice.route

import cats.effect.{ContextShift, IO, Timer}
import com.geirolz.microservice.model.{AppInfo, AppMetricsReport}
import com.geirolz.microservice.route.endpoint.infra.InfraEndpointsApi
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter

class InfraRoutes private (implicit C: ContextShift[IO], T: Timer[IO]) {

  import cats.implicits._
  import com.geirolz.microservice.route.endpoint.infra.contract.AppInfoContract._
  import com.geirolz.microservice.route.endpoint.infra.contract.AppMetricsReportContract._
  import com.geirolz.microservice.route.endpoint.util.ToContractMapper._

  private val getAppInfoRoute =
    Http4sServerInterpreter.toRoutes(InfraEndpointsApi.getAppInfo) { _ =>
      AppInfo.build.map(_.toContract.asRight[Unit])
    }

  private val getAppMetricsRoute =
    Http4sServerInterpreter.toRoutes(InfraEndpointsApi.getAppMetrics) { _ =>
      AppMetricsReport.fromCurrentRuntime.map(_.toContract.asRight[Unit])
    }

  val routes: HttpRoutes[IO] = getAppInfoRoute <+> getAppMetricsRoute

}
object InfraRoutes {
  def make(implicit C: ContextShift[IO], T: Timer[IO]): InfraRoutes = new InfraRoutes()
}
