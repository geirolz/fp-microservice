package com.geirolz.microservice.infra.endpoints.api

import com.geirolz.microservice.infra.endpoints.api.contracts.UserContract

object UserEndpointApi {

  import io.circe.generic.auto._
  import sttp.tapir._
  import sttp.tapir.generic.auto._
  import sttp.tapir.json.circe._

  val getUserById: Endpoint[Long, Unit, UserContract, Any] =
    endpoint.get
      .in(query[Long]("id"))
      .out(jsonBody[UserContract])
}
