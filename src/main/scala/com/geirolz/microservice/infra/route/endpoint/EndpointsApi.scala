package com.geirolz.microservice.infra.route.endpoint

import com.geirolz.microservice.infra.route.endpoint.infra.InfraEndpointsApi
import com.geirolz.microservice.infra.route.endpoint.user.UserEndpointApi
import com.geirolz.microservice.model.AppInfo
import sttp.tapir.Endpoint
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.circe.yaml.RichOpenAPI

private[route] object EndpointsApi {

  val all: Seq[Endpoint[_, _, _, _]] = Seq(
    UserEndpointApi.getById,
    InfraEndpointsApi.getAppInfo,
    InfraEndpointsApi.getAppMetrics
  )

  object OpenApi {
    val yaml: String = OpenAPIDocsInterpreter
      .toOpenAPI(
        es = EndpointsApi.all,
        title = AppInfo.value.name,
        version = AppInfo.value.version
      )
      .toYaml
  }
}
