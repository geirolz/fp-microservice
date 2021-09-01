package com.geirolz.microservice.infra.route.endpoint.user

import com.geirolz.microservice.common.route.endpoint.VersionedEndpoint
import com.geirolz.microservice.infra.route.endpoint.user.contract.{UserContract, UserEndpointError}
import com.geirolz.microservice.infra.route.endpoint.EndpointCustomInstances
import com.geirolz.microservice.model.values.UserId

private[route] object UserEndpointApi {

  import EndpointCustomInstances._
  import io.circe.generic.auto._
  import sttp.tapir._
  import sttp.tapir.generic.auto._
  import sttp.tapir.json.circe._

  private val user: Endpoint[Unit, Unit, Unit, Any] =
    VersionedEndpoint.v1.in("user")

  val getById: Endpoint[UserId, UserEndpointError, UserContract, Any] =
    user.get
      .in(
        path[UserId]("id")
          .description("User unique identifier")
          .validate(Validator.Min(0L, exclusive = true).contramap(_.value))
      )
      .out(jsonBody[UserContract])
      .errorOut(jsonBody[UserEndpointError])
}
