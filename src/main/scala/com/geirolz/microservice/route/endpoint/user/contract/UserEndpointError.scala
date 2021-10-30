package com.geirolz.microservice.route.endpoint.user.contract

import com.geirolz.microservice.model.values.UserId

private[route] sealed trait UserEndpointError
private[route] object UserEndpointError {
  case class UserNotFound(userId: UserId) extends UserEndpointError
}
