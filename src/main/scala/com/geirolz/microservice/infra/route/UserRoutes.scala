package com.geirolz.microservice.infra.route

import cats.data.NonEmptyList
import cats.effect.IO
import com.geirolz.microservice.common.data.{Endpoint, ModelMapper}
import com.geirolz.microservice.infra.route.endpoint.user.UserEndpointApi
import com.geirolz.microservice.infra.route.endpoint.user.contract.UserEndpointError
import com.geirolz.microservice.model.datatype.UserId
import com.geirolz.microservice.service.UserService
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter
import cats.effect.Temporal

class UserRoutes private (userService: UserService)(implicit CS: ContextShift[IO], T: Temporal[IO]) {

  import com.geirolz.microservice.infra.route.endpoint.user.contract.UserContract._
  import ModelMapper._
  import cats.implicits._

  private val getById: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(UserEndpointApi.getById)(userId => {
    userService
      .getById(userId)
      .map {
        case Some(user) => Right(user.toScopeId[Endpoint])
        case None       => Left(UserEndpointError.UserNotFound(userId))
      }
  })

  private val test: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(UserEndpointApi.test)(t => {
    IO.pure(Right[Unit, NonEmptyList[UserId]](NonEmptyList.one(t._1)))
  })

  val routes: HttpRoutes[IO] = getById <+> test
}
object UserRoutes {
  def make(userService: UserService)(implicit T: Temporal[IO]): UserRoutes =
    new UserRoutes(userService)
}
