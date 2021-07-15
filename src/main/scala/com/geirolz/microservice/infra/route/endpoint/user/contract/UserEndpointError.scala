package com.geirolz.microservice.infra.route.endpoint.user.contract

import com.geirolz.microservice.model.datatype.UserId

private[route] sealed trait UserEndpointError
private[route] object UserEndpointError {
  case class UserNotFound(userId: UserId) extends UserEndpointError
}
