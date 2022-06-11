package com.geirolz.fpmicroservice.route.endpoint

import com.geirolz.fpmicroservice.route.endpoint.infra.InfraEndpointsApi
import com.geirolz.fpmicroservice.route.endpoint.user.UserEndpointApi

object EndpointsApi {

  import sttp.tapir.*

  val all: List[AnyEndpoint] = List(
    UserEndpointApi.getById,
    InfraEndpointsApi.getAppInfo,
    InfraEndpointsApi.getAppMetrics
  ).asInstanceOf[List[AnyEndpoint]]

  object Versions {
    val v0: PublicEndpoint[Unit, Unit, Unit, Any] = v(0)

    private def v(version: Int): PublicEndpoint[Unit, Unit, Unit, Any] =
      endpoint.in("api" / s"v$version")
  }
}
