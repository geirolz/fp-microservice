package com.geirolz.microservice.route.endpoint.util

private[endpoint] object VersionedEndpoint {
  import sttp.tapir._

  val v1: Endpoint[Unit, Unit, Unit, Any] = v(1)

  private def v(version: Int): Endpoint[Unit, Unit, Unit, Any] = endpoint.in("api" / s"v$version")
}
