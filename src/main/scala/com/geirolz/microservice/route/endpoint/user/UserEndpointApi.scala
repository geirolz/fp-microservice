package com.geirolz.microservice.route.endpoint.user

import com.geirolz.microservice.common.route.endpoint.VersionedEndpoint
import com.geirolz.microservice.model.values.UserId
import com.geirolz.microservice.route.endpoint.user.contract.{UserContract, UserEndpointError}

private[route] object UserEndpointApi {

  import com.geirolz.microservice.route.endpoint.EndpointCustomInstances.*
  import io.circe.generic.auto.*
  import sttp.tapir.*
  import sttp.tapir.generic.auto.*
  import sttp.tapir.json.circe.*

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
