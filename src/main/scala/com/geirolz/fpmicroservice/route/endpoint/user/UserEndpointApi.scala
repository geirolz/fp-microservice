package com.geirolz.fpmicroservice.route.endpoint.user

import com.geirolz.fpmicroservice.common.route.endpoint.Api
import com.geirolz.fpmicroservice.model.values.UserId
import com.geirolz.fpmicroservice.route.endpoint.user.contract.{UserContract, UserEndpointError}

private[route] object UserEndpointApi {

  import io.circe.generic.auto.*
  import sttp.tapir.*
  import sttp.tapir.generic.auto.*
  import sttp.tapir.json.circe.*

  private val user: PublicEndpoint[Unit, Unit, Unit, Any] =
    Api.v1.in("user")

  val getById: PublicEndpoint[UserId, UserEndpointError, UserContract, Any] =
    user.get
      .in(
        path[UserId]("id")
          .description("User unique identifier")
          .validate(Validator.Min(0L, exclusive = true).contramap(_.value))
      )
      .out(jsonBody[UserContract])
      .errorOut(jsonBody[UserEndpointError])
}
