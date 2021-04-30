package com.geirolz.microservice.infra.endpoints.routes

import cats.effect.IO
import org.http4s.HttpRoutes

trait RoutesProvider {
  val routes: HttpRoutes[IO]
}
