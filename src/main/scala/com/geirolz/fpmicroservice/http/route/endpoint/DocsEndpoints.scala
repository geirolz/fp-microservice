package com.geirolz.fpmicroservice.http.route.endpoint

object DocsEndpoints {

  import io.circe.generic.auto.*
  import sttp.tapir.*

  val getJsonDocs: PublicEndpoint[Unit, Unit, String, Any] =
    endpoint
      .in("docs" / "swagger.json")
      .out(stringBody)

  val getYamlDocs: PublicEndpoint[Unit, Unit, String, Any] =
    endpoint
      .in("docs" / "swagger.yaml")
      .out(stringBody)
}
