package com.geirolz.microservice.infra.route.endpoint.user.contract

import com.geirolz.microservice.infra.route.endpoint.util.ToContractMapper
import com.geirolz.microservice.model.User

private[route] case class UserContract(id: Long, name: String, surname: String)
private[route] object UserContract {

  implicit val toContractMapper: ToContractMapper[User, UserContract] = user =>
    UserContract(user.id.value, user.name, user.surname)
}
