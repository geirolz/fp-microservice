package com.geirolz.fpmicroservice

import eu.timepit.refined.types.all.NonEmptyString

import java.time.LocalDateTime

trait AppInfo {
  val name: NonEmptyString
  val description: NonEmptyString
  val boundedContext: NonEmptyString
  val tags: Seq[NonEmptyString]
  val version: NonEmptyString
  val scalaVersion: NonEmptyString
  val sbtVersion: NonEmptyString
  val javaVersion: Option[NonEmptyString]
  val builtAt: LocalDateTime
  val buildNumber: NonEmptyString
  val buildRefName: NonEmptyString =
    NonEmptyString.unsafeFrom(s"$name:$version-$buildNumber $builtAt")
}
object AppInfo {

  def apply(
    name: NonEmptyString,
    description: NonEmptyString,
    boundedContext: NonEmptyString,
    tags: Seq[NonEmptyString],
    version: NonEmptyString,
    scalaVersion: NonEmptyString,
    sbtVersion: NonEmptyString,
    javaVersion: Option[NonEmptyString],
    builtAt: LocalDateTime,
    buildNumber: NonEmptyString
  ): AppInfo =
    new Basic(
      name           = name,
      description    = description,
      boundedContext = boundedContext,
      tags           = tags,
      version        = version,
      scalaVersion   = scalaVersion,
      sbtVersion     = sbtVersion,
      javaVersion    = javaVersion,
      builtAt        = builtAt,
      buildNumber    = buildNumber
    )

  class Basic(
    val name: NonEmptyString,
    val description: NonEmptyString,
    val boundedContext: NonEmptyString,
    val tags: Seq[NonEmptyString],
    val version: NonEmptyString,
    val scalaVersion: NonEmptyString,
    val sbtVersion: NonEmptyString,
    val javaVersion: Option[NonEmptyString],
    val builtAt: LocalDateTime,
    val buildNumber: NonEmptyString
  ) extends AppInfo
}
