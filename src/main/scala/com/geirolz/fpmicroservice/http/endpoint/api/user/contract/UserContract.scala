package com.geirolz.fpmicroservice.http.endpoint.api.user.contract

import com.geirolz.fpmicroservice.model.User
import com.geirolz.fpmicroservice.model.values.*
import io.circe.{Decoder, Encoder}
import scope.{ModelMapper, Scope}

private[endpoint] case class UserContract(
  id: UserId,
  email: Email,
  firstName: FirstName,
  middleName: Option[MiddleName],
  lastName: LastName
)
private[endpoint] object UserContract {

  import io.circe.generic.auto.*
  import io.circe.generic.semiauto.*

  // circe
  implicit val circeUserContractEncoder: Encoder[UserContract] = deriveEncoder[UserContract]
  implicit val circeUserContractDecoder: Decoder[UserContract] = deriveDecoder[UserContract]

  // scope
  implicit val scopeEndpointMapper: ModelMapper[Scope.Endpoint, User, UserContract] =
    ModelMapper.scoped { user =>
      UserContract(
        id         = user.id,
        email      = user.email,
        firstName  = user.firstName,
        middleName = user.middleName,
        lastName   = user.lastName
      )
    }
}
