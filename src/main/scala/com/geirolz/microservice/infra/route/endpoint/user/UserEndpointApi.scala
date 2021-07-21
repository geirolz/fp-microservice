package com.geirolz.microservice.infra.route.endpoint.user

import com.geirolz.microservice.common.route.endpoint.VersionedEndpoint
import com.geirolz.microservice.infra.route.endpoint.user.contract.{UserContract, UserEndpointError}
import com.geirolz.microservice.model.datatype.UserId

private[route] object UserEndpointApi {

  import com.geirolz.microservice.infra.route.endpoint.util.EndpointCommonCodecs._
  import io.circe.generic.auto._
  import sttp.tapir._
  import sttp.tapir.generic.auto._
  import sttp.tapir.json.circe._

  private val user: Endpoint[Unit, Unit, Unit, Any] =
    VersionedEndpoint.v1.in("user")

  val getById: Endpoint[UserId, UserEndpointError, UserContract, Any] =
    user.get
      .in(path[UserId])
      .out(jsonBody[UserContract])
      .errorOut(jsonBody[UserEndpointError])
}
