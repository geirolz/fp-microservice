package com.geirolz.fpmicroservice.http.endpoint.api.infra

import com.geirolz.fpmicroservice.http.endpoint.api.infra.contract.AppInfoContract

private[endpoint] object InfraEndpoints {

  import sttp.tapir.*
  import sttp.tapir.generic.auto.*
  import sttp.tapir.json.circe.*

  val healthcheck: PublicEndpoint[Unit, Unit, Unit, Any] =
    endpoint.get.in("healthcheck")

  val getAppInfo: PublicEndpoint[Unit, Unit, AppInfoContract, Any] =
    endpoint.get
      .in("info")
      .out(jsonBody[AppInfoContract])
}
