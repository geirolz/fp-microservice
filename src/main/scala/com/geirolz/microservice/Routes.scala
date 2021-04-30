package com.geirolz.microservice

import cats.effect.{ContextShift, IO, Timer}
import com.geirolz.microservice.infra.config.Config
import com.geirolz.microservice.infra.endpoints.routes.{InfraRoutes, RoutesProvider}
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.server.Router
import org.http4s.server.middleware.{RequestLogger, ResponseLogger}

class Routes(implicit C: ContextShift[IO], T: Timer[IO]) extends RoutesProvider {
  val routes: HttpRoutes[IO] = Router(
    "/infra" -> InfraRoutes.make.routes
  )
}
object Routes {

  import org.http4s.implicits._

  def makeApp(config: Config)(implicit C: ContextShift[IO], T: Timer[IO]): HttpApp[IO] = {
    val loggingConfig = config.http.server.logging
    val loggers: HttpApp[IO] => HttpApp[IO] = {
      { http: HttpApp[IO] =>
        RequestLogger.httpApp(
          logHeaders = loggingConfig.request.logHeaders,
          logBody = loggingConfig.request.logBody
        )(http)
      } andThen { http: HttpApp[IO] =>
        ResponseLogger.httpApp(
          logHeaders = loggingConfig.response.logHeaders,
          logBody = loggingConfig.response.logBody
        )(http)
      }
    }

    loggers(new Routes().routes.orNotFound)
  }
}
