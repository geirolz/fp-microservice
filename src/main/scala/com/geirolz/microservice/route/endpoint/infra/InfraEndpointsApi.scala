package com.geirolz.microservice.route.endpoint.infra

import com.geirolz.microservice.route.endpoint.infra.contract.{
  AppInfoContract,
  AppMetricsReportContract
}

private[route] object InfraEndpointsApi {

  import io.circe.generic.auto.*
  import sttp.tapir.*
  import sttp.tapir.generic.auto.*
  import sttp.tapir.json.circe.*

  val getAppMetrics: Endpoint[Unit, Unit, AppMetricsReportContract, Any] =
    endpoint.get
      .in("metrics")
      .out(jsonBody[AppMetricsReportContract])

  val getAppInfo: Endpoint[Unit, Unit, AppInfoContract, Any] =
    endpoint.get
      .in("info")
      .out(jsonBody[AppInfoContract])
}