package com.geirolz.fpmicroservice.route.endpoint.user.contract

import com.geirolz.fpmicroservice.model.values.UserId

private[route] sealed trait UserEndpointError
private[route] object UserEndpointError {
  case class UserNotFound(userId: UserId) extends UserEndpointError
}
