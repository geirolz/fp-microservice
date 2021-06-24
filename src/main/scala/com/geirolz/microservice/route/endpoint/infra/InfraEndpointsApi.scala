package com.geirolz.microservice.route.endpoint.infra

import com.geirolz.microservice.route.endpoint.infra.contract.{AppInfoContract, AppMetricsReportContract}
import com.geirolz.microservice.route.endpoint.util.VersionedEndpoint

private[route] object InfraEndpointsApi {

  import io.circe.generic.auto._
  import sttp.tapir._
  import sttp.tapir.generic.auto._
  import sttp.tapir.json.circe._

  private val infra: Endpoint[Unit, Unit, Unit, Any] =
    VersionedEndpoint.v1.in("infra")

  val getAppMetrics: Endpoint[Unit, Unit, AppMetricsReportContract, Any] =
    infra.get
      .in("metrics")
      .out(jsonBody[AppMetricsReportContract])

  val getAppInfo: Endpoint[Unit, Unit, AppInfoContract, Any] =
    infra.get
      .in("info")
      .out(jsonBody[AppInfoContract])
}
