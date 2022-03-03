package com.geirolz.microservice

import cats.effect.IO
import cats.implicits.toSemigroupKOps
import com.geirolz.microservice.route.{MainRoutes, UserRoutes}
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.server.middleware.{RequestLogger, ResponseLogger}

import scala.annotation.unused

class AppRoutes private (@unused config: Config, env: AppEnv) {
  val routes: HttpRoutes[IO] =
    MainRoutes.make.routes <+>
      UserRoutes.make(env.userService).routes
}
object AppRoutes {

  import org.http4s.implicits.*

  def makeApp(config: Config, env: AppEnv): HttpApp[IO] = {
    val loggingConfig = config.http.server.logging
    val loggers: HttpApp[IO] => HttpApp[IO] = {
      { http: HttpApp[IO] =>
        RequestLogger.httpApp(
          logHeaders = loggingConfig.request.logHeaders,
          logBody    = loggingConfig.request.logBody
        )(http)
      } andThen { http: HttpApp[IO] =>
        ResponseLogger.httpApp(
          logHeaders = loggingConfig.response.logHeaders,
          logBody    = loggingConfig.response.logBody
        )(http)
      }
    }

    loggers(new AppRoutes(config, env).routes.orNotFound)
  }
}
