package com.geirolz.fpmicroservice.model

import scala.util.Try
import com.geirolz.fpmicroservice.BuildInfo
import eu.timepit.refined.api.RefType.refinedRefType.refine
import eu.timepit.refined.types.string.NonEmptyString

case class AppInfo(
  name: NonEmptyString,
  description: NonEmptyString,
  tags: Seq[NonEmptyString],
  version: NonEmptyString,
  scalaVersion: NonEmptyString,
  sbtVersion: NonEmptyString,
  javaVersion: Option[NonEmptyString]
)
object AppInfo {

  val value: AppInfo =
    AppInfo(
      name         = refine.unsafeFrom(BuildInfo.name),
      description  = refine.unsafeFrom(BuildInfo.description),
      tags         = BuildInfo.projectInfoTags.map(refine.unsafeFrom(_)),
      version      = refine.unsafeFrom(BuildInfo.version),
      scalaVersion = refine.unsafeFrom(BuildInfo.scalaVersion),
      sbtVersion   = refine.unsafeFrom(BuildInfo.sbtVersion),
      javaVersion  = NonEmptyString.from(System.getProperty("java.version")).toOption
    )
}
