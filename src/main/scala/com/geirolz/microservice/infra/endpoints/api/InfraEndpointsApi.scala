package com.geirolz.microservice.infra.endpoints.api

import com.geirolz.microservice.infra.endpoints.api.contracts.HealthCheckContract

object InfraEndpointsApi {

  import io.circe.generic.auto._
  import sttp.tapir._
  import sttp.tapir.generic.auto._
  import sttp.tapir.json.circe._

  val healthCheck: Endpoint[Unit, Unit, HealthCheckContract, Any] =
    endpoint.out(jsonBody[HealthCheckContract])

}
