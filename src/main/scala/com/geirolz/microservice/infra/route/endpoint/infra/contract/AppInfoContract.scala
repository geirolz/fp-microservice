package com.geirolz.microservice.infra.route.endpoint.infra.contract

import com.geirolz.microservice.common.data.{Endpoint, ModelScopeMapper}
import com.geirolz.microservice.common.data.ModelScopeMapper.ModelScopeMapperId
import com.geirolz.microservice.model.AppInfo

private[route] case class AppInfoContract(
  name: String,
  version: String,
  scalaVersion: String,
  sbtVersion: String,
  javaVersion: Option[String]
)

private[route] object AppInfoContract {

  implicit val appInfoContractEndpointMapper: ModelScopeMapperId[Endpoint, AppInfo, AppInfoContract] =
    ModelScopeMapper.id(c =>
      AppInfoContract(
        name = c.name,
        version = c.version,
        scalaVersion = c.scalaVersion,
        sbtVersion = c.sbtVersion,
        javaVersion = c.javaVersion
      )
    )
}
