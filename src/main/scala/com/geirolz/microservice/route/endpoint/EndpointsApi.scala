package com.geirolz.microservice.route.endpoint

import com.geirolz.microservice.route.endpoint.infra.InfraEndpointsApi
import com.geirolz.microservice.route.endpoint.user.UserEndpointApi
import sttp.tapir.AnyEndpoint

object EndpointsApi {

  val all: List[AnyEndpoint] = List(
    UserEndpointApi.getById,
    InfraEndpointsApi.getAppInfo,
    InfraEndpointsApi.getAppMetrics
  ).asInstanceOf[List[AnyEndpoint]]
}
