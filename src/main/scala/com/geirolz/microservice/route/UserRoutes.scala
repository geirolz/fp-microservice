package com.geirolz.microservice.route

import cats.effect.IO
import com.geirolz.microservice.route.endpoint.user.UserEndpointApi
import com.geirolz.microservice.route.endpoint.user.contract.UserEndpointError
import com.geirolz.microservice.service.UserService
import com.geirolz.microservice.common.data.Scope
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter

class UserRoutes private (userService: UserService) {

  import com.geirolz.microservice.route.endpoint.user.contract.UserContract.*
  import com.geirolz.microservice.common.data.ModelMapper.*

  private val getById: HttpRoutes[IO] =
    Http4sServerInterpreter[IO]().toRoutes(UserEndpointApi.getById)(userId => {
      userService
        .getById(userId)
        .map {
          case Some(user) => Right(user.toScopeId[Scope.Endpoint])
          case None       => Left(UserEndpointError.UserNotFound(userId))
        }
    })

  val routes: HttpRoutes[IO] = getById
}
object UserRoutes {
  def make(userService: UserService): UserRoutes =
    new UserRoutes(userService)
}
