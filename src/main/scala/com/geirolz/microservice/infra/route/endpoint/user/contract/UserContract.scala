package com.geirolz.microservice.infra.route.endpoint.user.contract

import com.geirolz.microservice.common.data.{Endpoint, ModelMapper}
import com.geirolz.microservice.common.data.ModelMapper.ModelMapperId
import com.geirolz.microservice.model.User
import com.geirolz.microservice.model.values.UserId
import io.circe.{Decoder, Encoder}

private[route] case class UserContract(id: UserId, name: String, surname: String)
private[route] object UserContract {

  import com.geirolz.microservice.utils.CustomCirceCodecs._
  import io.circe.generic.auto._
  import io.circe.generic.semiauto._

  // circe
  implicit val circeUserContractEncoder: Encoder[UserContract] = deriveEncoder[UserContract]
  implicit val circeUserContractDecoder: Decoder[UserContract] = deriveDecoder[UserContract]

  implicit val toContractMapper: ModelMapperId[Endpoint, User, UserContract] =
    ModelMapper.lift { user =>
      UserContract(user.id, user.name, user.surname)
    }
}
