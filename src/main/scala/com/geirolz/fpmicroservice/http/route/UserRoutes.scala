package com.geirolz.fpmicroservice.http.route

import cats.effect.IO
import com.geirolz.fpmicroservice.http.route.endpoint.user.UserEndpoints
import com.geirolz.fpmicroservice.http.route.endpoint.user.contract.{
  UserContract,
  UserEndpointError
}
import com.geirolz.fpmicroservice.service.UserService
import scope.{InScope, Scope}
import sttp.tapir.server.http4s.Http4sServerInterpreter

class UserRoutes private (userService: UserService) extends InScope[Scope.Endpoint] {

  import scope.syntax.*
  import ServerRoutes.*

  private val interpreter: Http4sServerInterpreter[IO] =
    Http4sServerInterpreter[IO](AppRoutes.defaultServerOptions)

  private val getById: Http4sServerRoutes[IO] =
    UserEndpoints.getById
      .serverLogic[IO](userId => {
        userService
          .getById(userId)
          .map {
            case Some(user) => Right(user.scoped.as[UserContract])
            case None       => Left(UserEndpointError.UserNotFound(userId))
          }
      })
      .toRoutes(interpreter)

  val routes: Http4sServerRoutes[IO] = getById
}
object UserRoutes {
  def make(
    userService: UserService
  ): UserRoutes =
    new UserRoutes(userService)
}
