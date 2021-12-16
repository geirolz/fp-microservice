package com.geirolz.microservice.route

import cats.effect.IO
import com.geirolz.microservice.route.endpoint.user.UserEndpointApi
import com.geirolz.microservice.route.endpoint.user.contract.{UserContract, UserEndpointError}
import com.geirolz.microservice.service.UserService
import org.http4s.HttpRoutes
import scope.{Scope, ScopeContext, TypedScopeContext}
import sttp.tapir.server.http4s.Http4sServerInterpreter

class UserRoutes private (userService: UserService) {

  import scope.syntax.*

  implicit private val scopeCtx: TypedScopeContext[Scope.Endpoint] =
    ScopeContext.of[Scope.Endpoint]

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
