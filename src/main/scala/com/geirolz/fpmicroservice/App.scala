package com.geirolz.fpmicroservice

import cats.effect.{IO, IOApp}
import eu.timepit.refined.api.RefType.refinedRefType.refine
import eu.timepit.refined.types.all.NonEmptyString
import org.typelevel.log4cats.slf4j.Slf4jLogger

import java.time.{Instant, LocalDateTime, ZoneOffset}

object App extends IOApp.Simple {

  import pureconfig.*

  val info: AppInfo = AppInfo(
    name              = refine.unsafeFrom(BuildInfo.name),
    description       = refine.unsafeFrom(BuildInfo.description),
    boundedContext    = refine.unsafeFrom(BuildInfo.serviceBoundedContext),
    processingPurpose = refine.unsafeFrom(BuildInfo.serviceProcessingPurpose),
    tags              = BuildInfo.serviceTags.flatMap(NonEmptyString.from(_).toOption),
    version           = refine.unsafeFrom(BuildInfo.version),
    scalaVersion      = refine.unsafeFrom(BuildInfo.scalaVersion),
    sbtVersion        = refine.unsafeFrom(BuildInfo.sbtVersion),
    javaVersion       = NonEmptyString.from(System.getProperty("java.version")).toOption,
    builtAt = LocalDateTime.ofInstant(
      Instant.ofEpochMilli(BuildInfo.builtAtMillis),
      ZoneOffset.UTC
    ),
    buildNumber = refine.unsafeFrom(BuildInfo.buildInfoBuildNumber.toString)
  )

  override def run: IO[Unit] =
    AppBuilder.build(info)(
      logger                    = Slf4jLogger.getLogger[IO],
      configLoader              = IO(ConfigSource.default.loadOrThrow[AppConfig]),
      dependencyServicesBuilder = config => AppDependencyServices.make(config),
      providedServicesBuilder   = (config, services) => AppProvidedServices.make(config, services)
    )
}
