package com.geirolz.fpmicroservice.http.endpoint

import cats.effect.IO
import com.geirolz.fpmicroservice.model.values.UserId
import com.geirolz.fpmicroservice.testing.FakeUserService
import com.geirolz.fpmicroservice.testing.Samples.*
import org.http4s.{HttpRoutes, Method as Http4Method, Request, Response, Status}
import sttp.tapir.server.http4s.Http4sServerInterpreter

class UserServerEndpointsSuite extends munit.CatsEffectSuite {

  import org.http4s.implicits.*

  private val interpreter = Http4sServerInterpreter[IO]()

  test("Get user by Id route return the user info") {

    val routes: HttpRoutes[IO] =
      interpreter.toRoutes(
        UserServerEndpoints
          .make(
            FakeUserService.fromSeq(
              Seq(
                aUser(UserId(1))
              )
            )
          )
          .serverEndpoints
      )

    val result: IO[Response[IO]] = routes.orNotFound(
      Request(
        method = Http4Method.GET,
        uri    = uri"/api/v0/user/1"
      )
    )

    assertIO(
      obtained = result.map(_.status),
      returns  = Status.Ok
    )
  }

//  testEndpoint(routes)(UserEndpointApi.getById)(
//    UserId(1),
//    UserContract(
//      id         = aUserId,
//      email      = anEmail,
//      firstName  = aFirstName,
//      middleName = Some(aMiddleName),
//      lastName   = aLastName
//    )
//  )

//  def testEndpoint[I, E, O](routes: HttpRoutes[IO])(
//    endpoint: Endpoint[?, I, E, O, ?]
//  )(input: I, expected: O)(implicit loc: munit.Location): Unit = {
//    test("") {
//      routes.orNotFound(
//        Request(
//          method = endpoint.method match {
//            case Some(value) =>
//              value match {
//                case GET     => Http4Method.GET
//                case HEAD    => Http4Method.HEAD
//                case POST    => Http4Method.POST
//                case PUT     => Http4Method.PUT
//                case DELETE  => Http4Method.DELETE
//                case OPTIONS => Http4Method.OPTIONS
//                case PATCH   => Http4Method.PATCH
//                case CONNECT => Http4Method.CONNECT
//                case TRACE   => Http4Method.TRACE
//                case _       => ???
//              }
//            case None => ???
//          },
//          uri = {
//            Console.print(endpoint.renderPathTemplate())
//            Uri.unsafeFromString(endpoint.renderPathTemplate())
//          }
//        )
//      )
//
//      assert(1 == 1)
//    }
//  }
}
