package com.geirolz.fpmicroservice.http.endpoint.api

private[endpoint] object Endpoints {

  import sttp.tapir.*

  object Versions {
    val v0: PublicEndpoint[Unit, Unit, Unit, Any] = v(0)

    private def v(version: Int): PublicEndpoint[Unit, Unit, Unit, Any] =
      endpoint.in("api" / s"v$version")
  }
}
