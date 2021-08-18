package com.geirolz.microservice

import cats.effect.{IO, IOApp, Resource}
import com.comcast.ip4s.{Hostname, Port}
import com.geirolz.microservice.common.logging.FLog
import com.geirolz.microservice.infra.config.Config
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server

object App extends IOApp.Simple with FLog.IOLog with FLog.IOResourceLog {

  import cats.implicits._
  import pureconfig._
  import pureconfig.generic.auto._

  override def run: IO[Unit] =
    (
      for {
        //---------------- CONFIGURATION ----------------
        _      <- resLogger.info("Loading configuration...")
        config <- loadConfiguration
        _      <- resLogger.info(config.show)
        _      <- resLogger.info("Configuration successfully loaded.")

        //-------------------- ENV ----------------------
        _   <- resLogger.info("Building environment...")
        env <- Env.load(config)
        _   <- resLogger.info("Environment successfully built.")

        //-------------------- SERVER ----------------------
        _ <- resLogger.info("Building server...")
        server = buildServer(config, env)
        _ <- resLogger.info("Server successfully built.")
      } yield server
    ).use(server => {
      logger.info("Starting application...") >> server.useForever
    })

  private def loadConfiguration: Resource[IO, Config] =
    Resource.eval {
      ConfigSource.default.load[Config] match {
        case Left(failures) => IO.raiseError(new RuntimeException(failures.prettyPrint()))
        case Right(config)  => IO.pure(config)
      }
    }

  private def buildServer(config: Config, env: Env): Resource[IO, Server] =
    EmberServerBuilder
      .default[IO]
      .withHost(Hostname.fromString(config.http.server.host).get)
      .withPort(Port.fromInt(config.http.server.port).get)
      .withHttpApp(Routes.makeApp(config, env))
      .build
}
