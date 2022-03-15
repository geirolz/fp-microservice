package com.geirolz.fpmicroserivice.route

import cats.effect.IO
import com.geirolz.fpmicroserivice.testing.FakeUserService
import com.geirolz.fpmicroserivice.testing.Samples.aUser
import com.geirolz.fpmicroservice.model.values.UserId
import com.geirolz.fpmicroservice.route.UserRoutes
import org.http4s.{HttpRoutes, Method, Request, Response, Status}

class UserRoutesSuite extends munit.CatsEffectSuite {

  import org.http4s.implicits.*

  test("Get user by Id route return the user info") {

    val routes: HttpRoutes[IO] = UserRoutes
      .make(
        FakeUserService.fromSeq(
          Seq(
            aUser(UserId(1))
          )
        )
      )
      .routes

    val result: IO[Response[IO]] = routes.orNotFound(
      Request(
        method = Method.GET,
        uri    = uri"/api/v0/user/1"
      )
    )

    assertIO(
      obtained = result.map(_.status),
      returns  = Status.Ok
    )
  }
}
