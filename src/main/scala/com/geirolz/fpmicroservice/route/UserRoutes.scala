package com.geirolz.fpmicroservice.route

import cats.effect.IO
import com.geirolz.fpmicroservice.route.endpoint.user.UserEndpointApi
import com.geirolz.fpmicroservice.route.endpoint.user.contract.{UserContract, UserEndpointError}
import com.geirolz.fpmicroservice.service.UserService
import org.http4s.HttpRoutes
import scope.{InScope, Scope, ScopeContext, TypedScopeContext}
import sttp.tapir.server.http4s.Http4sServerInterpreter

class UserRoutes private (userService: UserService) extends InScope[Scope.Endpoint] {

  import scope.syntax.*

  private val interpreter: Http4sServerInterpreter[IO] =
    Http4sServerInterpreter[IO](ServerConfiguration.options)

  private val getById: HttpRoutes[IO] =
    interpreter.toRoutes(
      UserEndpointApi.getById.serverLogic[IO](userId => {
        userService
          .getById(userId)
          .map {
            case Some(user) => Right(user.scoped.as[UserContract])
            case None       => Left(UserEndpointError.UserNotFound(userId))
          }
      })
    )

  val routes: HttpRoutes[IO] = getById
}
object UserRoutes {
  def make(userService: UserService): UserRoutes =
    new UserRoutes(userService)
}
