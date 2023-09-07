package com.geirolz.fpmicroservice.http.endpoint.user.contract

import com.geirolz.fpmicroservice.model.User
import com.geirolz.fpmicroservice.model.values.*
import io.circe.{Codec, Decoder, Encoder}
import scope.{ModelMapper, Scope}

private[endpoint] case class UserDetailsResponse(
  id: UserId,
  email: Email,
  firstName: FirstName,
  middleName: Option[MiddleName],
  lastName: LastName
)
private[endpoint] object UserDetailsResponse {

  import io.circe.generic.auto.*
  import io.circe.generic.semiauto.*

  // json
  implicit val jsonCodec: Codec[UserDetailsResponse] =
    deriveCodec[UserDetailsResponse]

  // scope
  implicit val scopeEndpointMapper: ModelMapper[Scope.Endpoint, User, UserDetailsResponse] =
    ModelMapper.scoped { user =>
      UserDetailsResponse(
        id         = user.id,
        email      = user.email,
        firstName  = user.firstName,
        middleName = user.middleName,
        lastName   = user.lastName
      )
    }
}
