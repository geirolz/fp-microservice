package com.geirolz.fpmicroservice.http.endpoint.api.infra.contract

import com.geirolz.fpmicroservice.model.AppInfo
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import scope.{ModelMapper, Scope}

import java.time.LocalDateTime

private[endpoint] case class AppInfoContract(
  name: NonEmptyString,
  description: NonEmptyString,
  boundedContext: NonEmptyString,
  tags: Seq[NonEmptyString],
  version: NonEmptyString,
  scalaVersion: NonEmptyString,
  sbtVersion: NonEmptyString,
  javaVersion: Option[NonEmptyString],
  builtOn: LocalDateTime
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
        name           = c.name,
        description    = c.description,
        boundedContext = c.boundedContext,
        tags           = c.tags,
        version        = c.version,
        scalaVersion   = c.scalaVersion,
        sbtVersion     = c.sbtVersion,
        javaVersion    = c.javaVersion,
        builtOn        = c.builtOn
      )
    }
}
