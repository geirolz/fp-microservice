package com.geirolz.fpmicroservice.http.endpoint.api.infra.contract

import com.geirolz.fpmicroservice.AppInfo
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Codec
import scope.{ModelMapper, Scope}

import java.time.LocalDateTime

private[endpoint] case class AppInfoResponse(
  name: NonEmptyString,
  description: NonEmptyString,
  boundedContext: NonEmptyString,
  processingPurpose: NonEmptyString,
  tags: Seq[NonEmptyString],
  version: NonEmptyString,
  scalaVersion: NonEmptyString,
  sbtVersion: NonEmptyString,
  javaVersion: Option[NonEmptyString],
  builtAt: LocalDateTime,
  buildNumber: NonEmptyString,
  buildRefName: NonEmptyString
)

private[endpoint] object AppInfoResponse {

  import io.circe.generic.semiauto.*
  import io.circe.refined.*

  // json
  implicit val jsonCodec: Codec[AppInfoResponse] =
    deriveCodec[AppInfoResponse]

  // scope
  implicit val scopeEndpointMapper: ModelMapper[Scope.Endpoint, AppInfo, AppInfoResponse] =
    ModelMapper.scoped[Scope.Endpoint] { c =>
      AppInfoResponse(
        name              = c.name,
        description       = c.description,
        boundedContext    = c.boundedContext,
        processingPurpose = c.processingPurpose,
        tags              = c.tags,
        version           = c.version,
        scalaVersion      = c.scalaVersion,
        sbtVersion        = c.sbtVersion,
        javaVersion       = c.javaVersion,
        builtAt           = c.builtAt,
        buildNumber       = c.buildNumber,
        buildRefName      = c.buildRefName
      )
    }
}
