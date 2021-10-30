package com.geirolz.microservice.route.endpoint.infra.contract

import com.geirolz.microservice.common.data.{ModelMapper, Scope}
import com.geirolz.microservice.model.AppInfo

private[route] case class AppInfoContract(
  name: String,
  version: String,
  scalaVersion: String,
  sbtVersion: String,
  javaVersion: Option[String]
)

private[route] object AppInfoContract {

  implicit val scopeEndpointMapper: ModelMapper[Scope.Endpoint, AppInfo, AppInfoContract] =
    ModelMapper.forScope[Scope.Endpoint] { c =>
      AppInfoContract(
        name         = c.name,
        version      = c.version,
        scalaVersion = c.scalaVersion,
        sbtVersion   = c.sbtVersion,
        javaVersion  = c.javaVersion
      )
    }
}
