package com.geirolz.microservice.route

import cats.effect.{ContextShift, IO, Timer}
import com.geirolz.microservice.route.endpoint.user.UserEndpointApi
import com.geirolz.microservice.route.endpoint.user.UserEndpointApi.Errors
import com.geirolz.microservice.service.UserService
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter

class UserRoutes private (userService: UserService)(implicit C: ContextShift[IO], T: Timer[IO]) {

  import com.geirolz.microservice.route.endpoint.user.contract.UserContract._
  import com.geirolz.microservice.route.endpoint.util.ToContractMapper._

  val routes: HttpRoutes[IO] =
    Http4sServerInterpreter.toRoutes(UserEndpointApi.getById)(userId => {
      userService
        .getById(userId)
        .map {
          case Some(user) => Right(user.toContract)
          case None       => Left(Errors.UserNotFound(userId))
        }
    })
}
object UserRoutes {

  def make(userService: UserService)(implicit C: ContextShift[IO], T: Timer[IO]): UserRoutes = new UserRoutes(
    userService
  )
}
