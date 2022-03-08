package com.geirolz.fpmicroservice.route.endpoint

import com.geirolz.fpmicroservice.route.endpoint.infra.InfraEndpointsApi
import com.geirolz.fpmicroservice.route.endpoint.user.UserEndpointApi
import sttp.tapir.AnyEndpoint

object EndpointsApi {

  val all: List[AnyEndpoint] = List(
    UserEndpointApi.getById,
    InfraEndpointsApi.getAppInfo,
    InfraEndpointsApi.getAppMetrics
  ).asInstanceOf[List[AnyEndpoint]]
}
