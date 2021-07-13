package com.geirolz.microservice.infra.route.endpoint.user.contract

import com.geirolz.microservice.common.data.{Endpoint, ModelScopeMapper}
import com.geirolz.microservice.common.data.ModelScopeMapper.ModelScopeMapperId
import com.geirolz.microservice.model.User

private[route] case class UserContract(id: Long, name: String, surname: String)
private[route] object UserContract {

  implicit val toContractMapper: ModelScopeMapperId[Endpoint, User, UserContract] =
    ModelScopeMapper.id(user => UserContract(user.id.value, user.name, user.surname))
}
