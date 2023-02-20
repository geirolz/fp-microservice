package com.geirolz.fpmicroservice

import cats.Show
import com.geirolz.app.toolkit.SimpleAppInfo
import eu.timepit.refined.api.RefType.refinedRefType.refine
import eu.timepit.refined.types.all.NonEmptyString

import java.time.{Instant, LocalDateTime, ZoneOffset}

class AppInfo private (
  val name: NonEmptyString,
  val description: NonEmptyString,
  val boundedContext: NonEmptyString,
  val processingPurpose: NonEmptyString,
  val tags: Seq[NonEmptyString],
  val version: NonEmptyString,
  val scalaVersion: NonEmptyString,
  val sbtVersion: NonEmptyString,
  val javaVersion: Option[NonEmptyString],
  val builtOn: LocalDateTime,
  val buildNumber: NonEmptyString
) extends SimpleAppInfo[NonEmptyString] {
  override val buildRefName: NonEmptyString =
    NonEmptyString.unsafeFrom(
      SimpleAppInfo.genRefNameString[String](name.value, version.value, builtOn)
    )
}
object AppInfo {

  val fromBuildInfo: AppInfo = new AppInfo(
    name              = refine.unsafeFrom(BuildInfo.name),
    description       = refine.unsafeFrom(BuildInfo.description),
    boundedContext    = refine.unsafeFrom(BuildInfo.serviceBoundedContext),
    processingPurpose = refine.unsafeFrom(BuildInfo.serviceProcessingPurpose),
    tags              = BuildInfo.serviceTags.flatMap(NonEmptyString.from(_).toOption),
    version           = refine.unsafeFrom(BuildInfo.version),
    scalaVersion      = refine.unsafeFrom(BuildInfo.scalaVersion),
    sbtVersion        = refine.unsafeFrom(BuildInfo.sbtVersion),
    javaVersion       = NonEmptyString.from(System.getProperty("java.version")).toOption,
    builtOn = LocalDateTime.ofInstant(
      Instant.ofEpochMilli(BuildInfo.builtAtMillis),
      ZoneOffset.UTC
    ),
    buildNumber = refine.unsafeFrom(BuildInfo.buildInfoBuildNumber.toString)
  )

  implicit val show: Show[AppInfo] = Show.fromToString
}
