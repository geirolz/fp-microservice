package com.geirolz.fpmicroservice.http.endpoint.user

import cats.effect.IO
import com.geirolz.fpmicroservice.http.endpoint.Endpoints
import com.geirolz.fpmicroservice.http.endpoint.user.contract.UserEndpointError.{
  Unknown,
  UserNotFound
}
import com.geirolz.fpmicroservice.http.endpoint.user.contract.{
  UserDetailsResponse,
  UserEndpointError
}
import com.geirolz.fpmicroservice.model.values.UserId
import com.geirolz.fpmicroservice.service.UserService
import scope.{InScope, Scope}
import sttp.model.StatusCode
import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint

private[http] class UserEndpoints private (userService: UserService)
    extends InScope[Scope.Endpoint] {

  import scope.syntax.*

  private val getById: ServerEndpoint[Any, IO] =
    UserEndpoints.Def.getById
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
private[http] object UserEndpoints {

  def make(userService: UserService): UserEndpoints = new UserEndpoints(userService)

  object Def {

    import io.circe.generic.auto.*
    import sttp.tapir.generic.auto.*
    import sttp.tapir.json.circe.*

    private val user: PublicEndpoint[Unit, Unit, Unit, Any] =
      Endpoints.Versions.v0.in("user")

    val getById: PublicEndpoint[UserId, UserEndpointError, UserDetailsResponse, Any] =
      user.get
        .in(
          path[UserId]("id")
            .description("User unique identifier")
            .validate(Validator.Min(0L, exclusive = true).contramap(_.value))
        )
        .out(jsonBody[UserDetailsResponse])
        .errorOut(
          oneOf[UserEndpointError](
            oneOfVariant(
              statusCode(StatusCode.NotFound)
                .and(jsonBody[UserNotFound].description("not found"))
            ),
            oneOfDefaultVariant(
              jsonBody[Unknown].description("unknown")
            )
          )
        )
  }
}
