package com.geirolz.microservice.route.endpoint.user.contract

import com.geirolz.microservice.common.data.{ModelMapper, Scope}
import com.geirolz.microservice.model.User
import com.geirolz.microservice.model.values.UserId
import io.circe.{Decoder, Encoder}

private[route] case class UserContract(id: UserId, name: String, surname: String)
private[route] object UserContract {

  import io.circe.generic.auto.*
  import io.circe.generic.semiauto.*

  // circe
  implicit val circeUserContractEncoder: Encoder[UserContract] = deriveEncoder[UserContract]
  implicit val circeUserContractDecoder: Decoder[UserContract] = deriveDecoder[UserContract]

  implicit val scopeEndpointMapper: ModelMapper[Scope.Endpoint, User, UserContract] =
    ModelMapper.forScope[Scope.Endpoint] { user =>
      UserContract(user.id, user.name, user.surname)
    }
}
