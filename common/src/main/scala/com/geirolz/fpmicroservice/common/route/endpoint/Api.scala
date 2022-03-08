package com.geirolz.fpmicroservice.common.route.endpoint

object Api {

  import sttp.tapir.*

  val v1: PublicEndpoint[Unit, Unit, Unit, Any] = v(1)
  val v2: PublicEndpoint[Unit, Unit, Unit, Any] = v(2)
  val v3: PublicEndpoint[Unit, Unit, Unit, Any] = v(3)
  val v4: PublicEndpoint[Unit, Unit, Unit, Any] = v(4)
  val v5: PublicEndpoint[Unit, Unit, Unit, Any] = v(5)
  val v6: PublicEndpoint[Unit, Unit, Unit, Any] = v(6)

  private def v(version: Int): PublicEndpoint[Unit, Unit, Unit, Any] =
    endpoint.in("api" / s"v$version")
}
