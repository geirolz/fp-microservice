package com.geirolz.microservice.route.endpoint.infra.contract

import com.geirolz.microservice.model.AppInfo
import com.geirolz.microservice.route.endpoint.util.ToContractMapper

private[route] case class AppInfoContract(
  name: String,
  version: String,
  scalaVersion: String,
  sbtVersion: String,
  javaVersion: String
)

private[route] object AppInfoContract {

  implicit val appInfoContractMapper: ToContractMapper[AppInfo, AppInfoContract] =
    c =>
      AppInfoContract(
        name = c.name,
        version = c.version,
        scalaVersion = c.scalaVersion,
        sbtVersion = c.sbtVersion,
        javaVersion = c.javaVersion
      )
}
