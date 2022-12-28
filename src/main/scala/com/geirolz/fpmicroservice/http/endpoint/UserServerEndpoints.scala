package com.geirolz.fpmicroservice.http.endpoint

import cats.effect.IO
import com.geirolz.fpmicroservice.http.endpoint.api.user.UserEndpoints
import com.geirolz.fpmicroservice.http.endpoint.api.user.contract.{
  UserDetailsResponse,
  UserEndpointError
}
import com.geirolz.fpmicroservice.service.UserService
import scope.{InScope, Scope}
import sttp.tapir.server.ServerEndpoint

private[http] class UserServerEndpoints private (userService: UserService)
    extends InScope[Scope.Endpoint] {

  import scope.syntax.*

  private val getById: ServerEndpoint[Any, IO] =
    UserEndpoints.getById
      .serverLogic[IO](userId => {
        userService
          .getById(userId)
          .map {
            case Some(user) => Right(user.scoped.as[UserDetailsResponse])
            case None       => Left(UserEndpointError.UserNotFound(userId))
          }
      })

  val serverEndpoints: List[ServerEndpoint[Any, IO]] =
    List(getById)
}
private[http] object UserServerEndpoints {
  def make(
    userService: UserService
  ): UserServerEndpoints =
    new UserServerEndpoints(userService)
}
