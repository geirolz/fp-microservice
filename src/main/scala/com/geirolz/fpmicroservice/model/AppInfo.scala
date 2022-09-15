package com.geirolz.fpmicroservice.model

import com.geirolz.fpmicroservice.BuildInfo
import eu.timepit.refined.api.RefType.refinedRefType.refine
import eu.timepit.refined.types.string.NonEmptyString

import java.time.{Instant, LocalDateTime, ZoneOffset}

case class AppInfo(
  name: NonEmptyString,
  description: NonEmptyString,
  boundedContext: NonEmptyString,
  tags: Seq[NonEmptyString],
  version: NonEmptyString,
  scalaVersion: NonEmptyString,
  sbtVersion: NonEmptyString,
  javaVersion: Option[NonEmptyString],
  builtOn: LocalDateTime
) {
  val buildRefName: String = s"$name:$version-$builtOn"
}
object AppInfo {

  val value: AppInfo =
    AppInfo(
      name           = refine.unsafeFrom(BuildInfo.name),
      description    = refine.unsafeFrom(BuildInfo.description),
      boundedContext = refine.unsafeFrom(BuildInfo.boundedContext),
      tags           = BuildInfo.infoTags.map(refine.unsafeFrom(_)),
      version        = refine.unsafeFrom(BuildInfo.version),
      scalaVersion   = refine.unsafeFrom(BuildInfo.scalaVersion),
      sbtVersion     = refine.unsafeFrom(BuildInfo.sbtVersion),
      javaVersion    = NonEmptyString.from(System.getProperty("java.version")).toOption,
      builtOn = LocalDateTime.ofInstant(Instant.ofEpochMilli(BuildInfo.builtOn), ZoneOffset.UTC)
    )
}
