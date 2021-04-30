package com.geirolz.microservice

import cats.effect.{ExitCode, IO, IOApp, Sync}
import com.geirolz.microservice.infra.config.Config
import org.http4s.server.blaze.BlazeServerBuilder
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.ExecutionContext

object App extends IOApp {

  // Impure But What 90% of Folks I know do with log4s
  implicit def unsafeLogger[F[_]: Sync]: SelfAwareStructuredLogger[F] = Slf4jLogger.getLogger[F]
  import pureconfig._
  import pureconfig.generic.auto._

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- Logger[IO].info("Starting application...")

      //---------------- CONFIGURATION ----------------
      _      <- Logger[IO].info("Loading configuration...")
      config <- loadConfiguration
      _      <- Logger[IO].info("Configuration successfully loaded.")

      //------------------- SERVER ----------------
      _ <- Logger[IO].info("Building app server...")
      server = buildServer(config)
      _ <- server.serve.compile.drain
      _ <- Logger[IO].info("Shutting down application...")
    } yield ExitCode.Success
  }

  private val loadConfiguration: IO[Config] =
    ConfigSource.default.load[Config] match {
      case Left(failures) => IO.raiseError(new RuntimeException(failures.prettyPrint()))
      case Right(config)  => IO.pure(config)
    }

  private def buildServer(config: Config): BlazeServerBuilder[IO] =
    BlazeServerBuilder[IO](ExecutionContext.global)
      .bindHttp(config.http.server.port, config.http.server.host)
      .withBanner(customBanner)
      .withHttpApp(Routes.makeApp(config))

  //generated by https://devops.datenkollektiv.de/banner.txt/index.html
  private val customBanner: Seq[String] =
    """
      |  ______         _                _        
      | / _____)       (_)              | |       
      || /  ___   ____  _   ____   ___  | | _____ 
      || | (___) / _  )| | / ___) / _ \ | |(___  )
      || \____/|( (/ / | || |    | |_| || | / __/ 
      | \_____/  \____)|_||_|     \___/ |_|(_____)""".stripMargin.split("\n").toList
}
