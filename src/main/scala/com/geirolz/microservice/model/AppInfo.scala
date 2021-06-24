package com.geirolz.microservice.model

import cats.effect.IO
import com.geirolz.microservice.BuildInfo

case class AppInfo(
  name: String,
  version: String,
  javaVersion: String,
  scalaVersion: String,
  sbtVersion: String
)
object AppInfo {
  def build: IO[AppInfo] =
    for {
      javaVersion <- IO.fromOption(Option(System.getProperty("java.version")))(
        new RuntimeException("Unable to detect Java version")
      )
    } yield AppInfo(
      name = BuildInfo.name,
      version = BuildInfo.version,
      javaVersion = BuildInfo.scalaVersion,
      scalaVersion = BuildInfo.sbtVersion,
      sbtVersion = javaVersion
    )
}
