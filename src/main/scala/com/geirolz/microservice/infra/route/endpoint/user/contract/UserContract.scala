package com.geirolz.microservice.infra.route.endpoint.user.contract

import com.geirolz.microservice.common.data.{Endpoint, ModelMapper}
import com.geirolz.microservice.common.data.ModelMapper.ModelMapperId
import com.geirolz.microservice.model.User

private[route] case class UserContract(id: Long, name: String, surname: String)
private[route] object UserContract {

  implicit val toContractMapper: ModelMapperId[Endpoint, User, UserContract] =
    ModelMapper.id(user => UserContract(user.id.value, user.name, user.surname))
}
