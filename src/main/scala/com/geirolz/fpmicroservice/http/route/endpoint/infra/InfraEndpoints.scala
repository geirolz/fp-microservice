package com.geirolz.fpmicroservice.http.route.endpoint.infra

import com.geirolz.fpmicroservice.http.route.endpoint.infra.contract.{
  AppInfoContract,
  AppMetricsReportContract
}

private[route] object InfraEndpoints {

  import io.circe.generic.auto.*
  import sttp.tapir.*
  import sttp.tapir.generic.auto.*
  import sttp.tapir.json.circe.*

  val healthcheck: PublicEndpoint[Unit, Unit, Unit, Any] =
    endpoint.get.in("healthcheck")

  val getAppMetrics: PublicEndpoint[Unit, Unit, AppMetricsReportContract, Any] =
    endpoint.get
      .in("metrics")
      .out(jsonBody[AppMetricsReportContract])

  val getAppInfo: PublicEndpoint[Unit, Unit, AppInfoContract, Any] =
    endpoint.get
      .in("info")
      .out(jsonBody[AppInfoContract])
}
