package com.geirolz.fpmicroservice.http.endpoint.api.user

import com.geirolz.fpmicroservice.http.endpoint.api.user.contract.{UserContract, UserEndpointError}
import com.geirolz.fpmicroservice.http.endpoint.api.user.contract.UserEndpointError.{
  Unknown,
  UserNotFound
}
import com.geirolz.fpmicroservice.http.endpoint.api.Endpoints
import com.geirolz.fpmicroservice.model.values.UserId
import sttp.model.StatusCode

private[endpoint] object UserEndpoints {

  import io.circe.generic.auto.*
  import sttp.tapir.*
  import sttp.tapir.generic.auto.*
  import sttp.tapir.json.circe.*

  private val user: PublicEndpoint[Unit, Unit, Unit, Any] =
    Endpoints.Versions.v0.in("user")

  val getById: PublicEndpoint[UserId, UserEndpointError, UserContract, Any] =
    user.get
      .in(
        path[UserId]("id")
          .description("User unique identifier")
          .validate(Validator.Min(0L, exclusive = true).contramap(_.value))
      )
      .out(jsonBody[UserContract])
      .errorOut(
        oneOf[UserEndpointError](
          oneOfVariant(
            statusCode(StatusCode.NotFound)
              .and(jsonBody[UserNotFound].description("not found"))
          ),
          oneOfDefaultVariant(
            jsonBody[Unknown].description("unknown")
          )
        )
      )
}
