package com.geirolz.fpmicroservice.route.endpoint.infra.contract

import com.geirolz.fpmicroservice.model.AppInfo
import scope.{ModelMapper, Scope}

private[route] case class AppInfoContract(
  name: String,
  version: String,
  scalaVersion: String,
  sbtVersion: String,
  javaVersion: Option[String]
)

private[route] object AppInfoContract {

  implicit val scopeEndpointMapper: ModelMapper[Scope.Endpoint, AppInfo, AppInfoContract] =
    ModelMapper.scoped[Scope.Endpoint] { c =>
      AppInfoContract(
        name         = c.name,
        version      = c.version,
        scalaVersion = c.scalaVersion,
        sbtVersion   = c.sbtVersion,
        javaVersion  = c.javaVersion
      )
    }
}
