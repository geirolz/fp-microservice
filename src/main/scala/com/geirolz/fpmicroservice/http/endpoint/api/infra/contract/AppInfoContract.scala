package com.geirolz.fpmicroservice.http.endpoint.api.infra.contract

import com.geirolz.fpmicroservice.model.AppInfo
import scope.{ModelMapper, Scope}

private[endpoint] case class AppInfoContract(
  name: String,
  version: String,
  scalaVersion: String,
  sbtVersion: String,
  javaVersion: Option[String]
)

private[endpoint] object AppInfoContract {

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
