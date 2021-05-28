package com.geirolz.microservice.route

import cats.effect.{ContextShift, IO, Timer}
import com.geirolz.microservice.route.endpoint.infra.contract.HealthCheckReportContract
import com.geirolz.microservice.route.endpoint.infra.InfraEndpointsApi
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter

class InfraRoutes private (implicit C: ContextShift[IO], T: Timer[IO]) {

  import cats.implicits._

  val routes: HttpRoutes[IO] =
    Http4sServerInterpreter
      .toRoutes(InfraEndpointsApi.getHealthCheck)(_ => IO(HealthCheckReportContract("v1").asRight[Unit]))

}
object InfraRoutes {
  def make(implicit C: ContextShift[IO], T: Timer[IO]): InfraRoutes = new InfraRoutes()
}
