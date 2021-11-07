package com.geirolz.microservice.common.route.endpoint

object Api {

  import sttp.tapir.*

  val v1: Endpoint[Unit, Unit, Unit, Any] = v(1)
  val v2: Endpoint[Unit, Unit, Unit, Any] = v(2)
  val v3: Endpoint[Unit, Unit, Unit, Any] = v(3)
  val v4: Endpoint[Unit, Unit, Unit, Any] = v(4)
  val v5: Endpoint[Unit, Unit, Unit, Any] = v(5)
  val v6: Endpoint[Unit, Unit, Unit, Any] = v(6)

  private def v(version: Int): Endpoint[Unit, Unit, Unit, Any] = endpoint.in("api" / s"v$version")
}
