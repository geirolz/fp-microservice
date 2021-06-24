package com.geirolz.microservice.route.endpoint.infra

import com.geirolz.microservice.route.endpoint.infra.contract.HealthCheckReportContract
import com.geirolz.microservice.route.endpoint.VersionedEndpoint

private[route] object InfraEndpointsApi {

  import io.circe.generic.auto._
  import sttp.tapir._
  import sttp.tapir.generic.auto._
  import sttp.tapir.json.circe._

  private val infra: Endpoint[Unit, Unit, Unit, Any] =
    VersionedEndpoint.v1.in("infra")

  val getHealthCheck: Endpoint[Unit, Unit, HealthCheckReportContract, Any] =
    infra.get
      .in("health")
      .out(jsonBody[HealthCheckReportContract])
}
