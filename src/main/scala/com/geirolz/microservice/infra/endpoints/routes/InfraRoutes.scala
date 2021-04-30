package com.geirolz.microservice.infra.endpoints.routes

import cats.effect.{ContextShift, IO, Timer}
import com.geirolz.microservice.infra.endpoints.api.InfraEndpointsApi
import com.geirolz.microservice.infra.endpoints.api.contracts.HealthCheckContract
import org.http4s.HttpRoutes
import org.http4s.server.Router
import sttp.tapir.server.http4s.Http4sServerInterpreter

class InfraRoutes(implicit C: ContextShift[IO], T: Timer[IO]) extends RoutesProvider {

  import cats.implicits._
  import InfraEndpointsApi._

  val routes: HttpRoutes[IO] = Router(
    "/health" -> Http4sServerInterpreter.toRoutes(healthCheck)(_ => IO(HealthCheckContract("v1").asRight[Unit]))
  )
}
object InfraRoutes {
  def make(implicit C: ContextShift[IO], T: Timer[IO]): InfraRoutes = new InfraRoutes()
}
