package com.geirolz.microservice.infra.route.endpoint.user.contract

import com.geirolz.microservice.common.data.{Endpoint, ModelMapper}
import com.geirolz.microservice.common.data.ModelMapper.ModelMapperId
import com.geirolz.microservice.model.User
import com.geirolz.microservice.model.datatype.UserId

private[route] case class UserContract(id: UserId, name: String, surname: String)
private[route] object UserContract {

  implicit val toContractMapper: ModelMapperId[Endpoint, User, UserContract] =
    ModelMapper.lift { user =>
      UserContract(user.id, user.name, user.surname)
    }
}
