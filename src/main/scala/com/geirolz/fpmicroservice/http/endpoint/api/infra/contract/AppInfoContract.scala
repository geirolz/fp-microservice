package com.geirolz.fpmicroservice.http.endpoint.api.infra.contract

import com.geirolz.fpmicroservice.model.AppInfo
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import scope.{ModelMapper, Scope}

private[endpoint] case class AppInfoContract(
  name: NonEmptyString,
  description: NonEmptyString,
  tags: Seq[NonEmptyString],
  version: NonEmptyString,
  scalaVersion: NonEmptyString,
  sbtVersion: NonEmptyString,
  javaVersion: Option[NonEmptyString]
)

private[endpoint] object AppInfoContract {

  import io.circe.generic.semiauto.*
  import io.circe.refined.*

  // json
  implicit val jsonCoder: Codec[AppInfoContract] =
    deriveCodec[AppInfoContract]

  // scope
  implicit val scopeEndpointMapper: ModelMapper[Scope.Endpoint, AppInfo, AppInfoContract] =
    ModelMapper.scoped[Scope.Endpoint] { c =>
      AppInfoContract(
        name         = c.name,
        version      = c.version,
        description  = c.description,
        tags         = c.tags,
        scalaVersion = c.scalaVersion,
        sbtVersion   = c.sbtVersion,
        javaVersion  = c.javaVersion
      )
    }
}
