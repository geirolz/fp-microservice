package com.geirolz.microservice

import cats.effect.{ExitCode, IO, IOApp}
import com.geirolz.microservice.infra.config.Config
import org.http4s.server.blaze.BlazeServerBuilder
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.ExecutionContext

object App extends IOApp {

  import pureconfig._
  import pureconfig.generic.auto._

  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- logger.info("Starting application...")

      //---------------- CONFIGURATION ----------------
      _      <- logger.info("Loading configuration...")
      config <- loadConfiguration
      _      <- logger.info(config.toString)
      _      <- logger.info("Configuration successfully loaded.")

      //-------------------- ENV ----------------------
      _   <- logger.info("Building environment...")
      env <- Env.load(config)
      _   <- logger.info("Environment successfully built.")

      //------------------- SERVER --------------------
      _ <- logger.info("Building app server...")
      server = buildServer(config, env)
      _ <- server.serve.compile.drain
    } yield ExitCode.Success
  }

  private def loadConfiguration: IO[Config] =
    ConfigSource.default.load[Config] match {
      case Left(failures) => IO.raiseError(new RuntimeException(failures.prettyPrint()))
      case Right(config)  => IO.pure(config)
    }

  private def buildServer(config: Config, env: Env): BlazeServerBuilder[IO] =
    BlazeServerBuilder[IO](ExecutionContext.global)
      .bindHttp(config.http.server.port, config.http.server.host)
      .withBanner(customBanner)
      .withHttpApp(Routes.makeApp(config, env))

  //generated by https://devops.datenkollektiv.de/banner.txt/index.html
  private val customBanner: Seq[String] =
    """
      |    ______    ____           _____                         _              
      |   / ____/   / __ \         / ___/  ___    _____ _   __   (_)  _____  ___ 
      |  / /_      / /_/ /         \__ \  / _ \  / ___/| | / /  / /  / ___/ / _ \
      | / __/     / ____/         ___/ / /  __/ / /    | |/ /  / /  / /__  /  __/
      |/_/       /_/             /____/  \___/ /_/     |___/  /_/   \___/  \___/""".stripMargin.split("\n").toList
}
