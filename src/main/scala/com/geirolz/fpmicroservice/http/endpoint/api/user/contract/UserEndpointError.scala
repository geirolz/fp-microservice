package com.geirolz.fpmicroservice.http.endpoint.api.user.contract

import com.geirolz.fpmicroservice.model.values.UserId

private[endpoint] sealed trait UserEndpointError
private[endpoint] object UserEndpointError {
  case class UserNotFound(userId: UserId) extends UserEndpointError
  case class Unknown(code: Int, msg: String) extends UserEndpointError
}
