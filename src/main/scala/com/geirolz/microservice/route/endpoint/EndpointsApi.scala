package com.geirolz.microservice.route.endpoint

import com.geirolz.microservice.route.endpoint.infra.InfraEndpointsApi
import com.geirolz.microservice.route.endpoint.user.UserEndpointApi
import sttp.tapir.Endpoint

object EndpointsApi {

  val all: List[Endpoint[?, ?, ?, ?]] = List(
    UserEndpointApi.getById,
    InfraEndpointsApi.getAppInfo,
    InfraEndpointsApi.getAppMetrics
  )
}
