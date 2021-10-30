package com.geirolz.microservice.route.endpoint.user.contract

import com.geirolz.microservice.common.data.{ModelMapper, Scope}
import com.geirolz.microservice.common.data.ModelMapper.ModelMapperId
import com.geirolz.microservice.model.User
import com.geirolz.microservice.model.values.UserId
import com.geirolz.microservice.utils.CirceCustomUnwrapped
import io.circe.{Decoder, Encoder}

private[route] case class UserContract(id: UserId, name: String, surname: String)
private[route] object UserContract {

  import io.circe.generic.auto.*
  import io.circe.generic.semiauto.*
  import CirceCustomUnwrapped.*

  // circe
  implicit val circeUserContractEncoder: Encoder[UserContract] = deriveEncoder[UserContract]
  implicit val circeUserContractDecoder: Decoder[UserContract] = deriveDecoder[UserContract]

  implicit val toContractMapper: ModelMapperId[Scope.Endpoint, User, UserContract] =
    ModelMapper.lift { user =>
      UserContract(user.id, user.name, user.surname)
    }
}
