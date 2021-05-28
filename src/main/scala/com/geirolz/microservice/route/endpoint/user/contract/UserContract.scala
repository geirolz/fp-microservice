package com.geirolz.microservice.route.endpoint.user.contract

import com.geirolz.microservice.model.User
import com.geirolz.microservice.route.util.ToContractMapper

private[route] case class UserContract(id: Long, name: String, surname: String)
private[route] object UserContract {

  implicit val toContractMapper: ToContractMapper[User, UserContract] = user =>
    UserContract(user.id.value, user.name, user.surname)
}
