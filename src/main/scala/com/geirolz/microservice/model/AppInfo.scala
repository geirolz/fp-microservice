package com.geirolz.microservice.model

import com.geirolz.microservice.BuildInfo

import scala.util.Try

case class AppInfo(
  name: String,
  version: String,
  scalaVersion: String,
  sbtVersion: String,
  javaVersion: Option[String]
)
object AppInfo {
  val value: AppInfo =
    AppInfo(
      name = BuildInfo.name,
      version = BuildInfo.version,
      scalaVersion = BuildInfo.scalaVersion,
      sbtVersion = BuildInfo.sbtVersion,
      javaVersion = Try(Option(System.getProperty("java.version"))).toOption.flatten
    )
}
