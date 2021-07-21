package com.geirolz.microservice.infra.route

import cats.effect.{ContextShift, IO, Timer}
import com.geirolz.microservice.common.data.{Endpoint, ModelMapper}
import com.geirolz.microservice.infra.route.endpoint.user.UserEndpointApi
import com.geirolz.microservice.infra.route.endpoint.user.contract.UserEndpointError
import com.geirolz.microservice.service.UserService
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter

class UserRoutes private (userService: UserService)(implicit CS: ContextShift[IO], T: Timer[IO]) {

  import com.geirolz.microservice.infra.route.endpoint.user.contract.UserContract._
  import ModelMapper._

  private val getById: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(UserEndpointApi.getById)(userId => {
    userService
      .getById(userId)
      .map {
        case Some(user) => Right(user.toScopeId[Endpoint])
        case None       => Left(UserEndpointError.UserNotFound(userId))
      }
  })

  val routes: HttpRoutes[IO] = getById
}
object UserRoutes {
  def make(userService: UserService)(implicit C: ContextShift[IO], T: Timer[IO]): UserRoutes =
    new UserRoutes(userService)
}
