package com.geirolz.microservice.infra.route

import com.geirolz.microservice.infra.route.endpoint.user.contract.UserContract
import com.geirolz.microservice.model.values.UserId
import com.geirolz.microservice.model.User
import com.geirolz.microservice.service.UserService
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UserRoutesTest extends AnyWordSpec with Matchers {

  import cats.effect._
  import cats.effect.unsafe.implicits.global
  import com.geirolz.microservice.infra.route.endpoint.EndpointCustomInstances._
  import io.circe.generic.auto._
  import org.http4s._
  import org.http4s.circe._
  import org.http4s.implicits._

  "UserRoutes getById route" when {
    "invoked with existing user Id" should {
      "reply with the user information" in {

        //given
        val userService = new UserService {
          override def getById(id: UserId): IO[Option[User]] =
            IO.pure(
              Some(
                User(
                  id = id,
                  name = "Mario",
                  surname = "Rossi"
                )
              )
            )
        }

        val routes: HttpRoutes[IO] = UserRoutes.make(userService).routes
        val request: Request[IO] = Request(
          method = Method.GET,
          uri = uri"api/v1/user/1"
        )

        //exec
        val response: Response[IO] = routes.orNotFound.run(request).unsafeRunSync()

        //asserts
        response.status shouldBe Status.Ok
        response.decodeJson[UserContract].unsafeRunSync() shouldBe UserContract(
          id = UserId(1),
          name = "Mario",
          surname = "Rossi"
        )
      }
    }
  }
}
