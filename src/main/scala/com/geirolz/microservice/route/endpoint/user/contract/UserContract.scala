package com.geirolz.microservice.route.endpoint.user.contract

import com.geirolz.microservice.model.User
import com.geirolz.microservice.model.values.{FirstName, LastName, MiddleName, UserId}
import io.circe.{Decoder, Encoder}
import scope.{ModelMapper, Scope}

private[route] case class UserContract(
  id: UserId,
  firstName: FirstName,
  middleName: Option[MiddleName],
  lastName: LastName
)
private[route] object UserContract {

  import io.circe.generic.auto.*
  import io.circe.generic.semiauto.*

  // circe
  implicit val circeUserContractEncoder: Encoder[UserContract] = deriveEncoder[UserContract]
  implicit val circeUserContractDecoder: Decoder[UserContract] = deriveDecoder[UserContract]

  implicit val scopeEndpointMapper: ModelMapper[Scope.Endpoint, User, UserContract] =
    ModelMapper.scoped[Scope.Endpoint] { user =>
      UserContract(
        id         = user.id,
        firstName  = user.firstName,
        middleName = user.middleName,
        lastName   = user.lastName
      )
    }
}
