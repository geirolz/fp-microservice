package com.geirolz.microservice.infra.route

import cats.effect.{ContextShift, IO, Timer}
import com.geirolz.microservice.common.data.Endpoint
import com.geirolz.microservice.infra.route.endpoint.infra.InfraEndpointsApi
import com.geirolz.microservice.infra.route.endpoint.EndpointsApi
import com.geirolz.microservice.model.{AppInfo, AppMetricsReport}
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.http4s.SwaggerHttp4s

class MainRoutes private (implicit C: ContextShift[IO], T: Timer[IO]) {

  import cats.implicits._
  import com.geirolz.microservice.common.data.ModelMapper._
  import com.geirolz.microservice.infra.route.endpoint.infra.contract.AppInfoContract._
  import com.geirolz.microservice.infra.route.endpoint.infra.contract.AppMetricsReportContract._

  private val appInfoRoute =
    Http4sServerInterpreter.toRoutes(InfraEndpointsApi.getAppInfo) { _ =>
      IO.pure(AppInfo.value.toScopeId[Endpoint].asRight[Unit])
    }

  private val appMetricsRoute =
    Http4sServerInterpreter.toRoutes(InfraEndpointsApi.getAppMetrics) { _ =>
      AppMetricsReport.fromCurrentRuntime.map(_.toScopeId[Endpoint].asRight[Unit])
    }

  private val swaggerRoute =
    new SwaggerHttp4s(EndpointsApi.OpenApi.yaml).routes[IO]

  val routes: HttpRoutes[IO] =
    appInfoRoute <+>
    appMetricsRoute <+>
    swaggerRoute
}
object MainRoutes {
  def make(implicit C: ContextShift[IO], T: Timer[IO]): MainRoutes = new MainRoutes()
}
