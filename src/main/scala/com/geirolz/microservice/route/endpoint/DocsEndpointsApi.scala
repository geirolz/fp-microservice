package com.geirolz.microservice.route.endpoint

object DocsEndpointsApi {

  import io.circe.generic.auto.*
  import sttp.tapir.*

  val getJsonDocs: Endpoint[Unit, Unit, String, Any] =
    endpoint
      .in("docs" / "swagger.json")
      .out(stringBody)

  val getYamlDocs: Endpoint[Unit, Unit, String, Any] =
    endpoint
      .in("docs" / "swagger.yaml")
      .out(stringBody)
}
