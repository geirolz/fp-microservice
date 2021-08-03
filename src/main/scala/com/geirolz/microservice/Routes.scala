package com.geirolz.microservice

import cats.effect.IO
import cats.implicits.toSemigroupKOps
import com.geirolz.microservice.infra.config.Config
import com.geirolz.microservice.infra.route.{MainRoutes, UserRoutes}
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.server.middleware.{RequestLogger, ResponseLogger}

import scala.annotation.unused

class Routes private (@unused config: Config, env: Env) {
  val routes: HttpRoutes[IO] =
    MainRoutes.make.routes <+>
    UserRoutes.make(env.userService).routes
}
object Routes {

  import org.http4s.implicits._

  def makeApp(config: Config, env: Env): HttpApp[IO] = {
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

    loggers(new Routes(config, env).routes.orNotFound)
  }
}
