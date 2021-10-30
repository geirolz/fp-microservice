package com.geirolz.microservice.route.endpoint

import cats.MonadError
import cats.effect.IO
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import sttp.tapir.testing.{FindShadowedEndpoints, ShadowedEndpoint}
import sttp.tapir.Endpoint

class FindShadowedEndpointTest extends AnyWordSpec with Matchers {

  import cats.effect.unsafe.implicits.global

  "Application endpoints" should {
    "not have shadowing" in {
      ShadowedEndpoints.check[IO](EndpointsApi.all).unsafeRunSync()
    }
  }
}

case class ShadowedEndpointException(
  endpoints: List[Endpoint[?, ?, ?, ?]],
  ses: Set[ShadowedEndpoint]
) extends RuntimeException(
      s"${ses.size} endpoints shadowed.\n${ShadowedEndpoints.asPrettyString(ses)}"
    )

object ShadowedEndpoints {

  def check[F[_]](
    endpoints: List[Endpoint[?, ?, ?, ?]]
  )(implicit F: MonadError[F, Throwable]): F[Unit] = {
    find(endpoints) match {
      case ses if ses.isEmpty => F.pure(())
      case ses                => F.raiseError(ShadowedEndpointException(endpoints, ses))
    }
  }

  def find(endpoints: List[Endpoint[?, ?, ?, ?]]): Set[ShadowedEndpoint] = FindShadowedEndpoints(
    endpoints
  )

  def asPrettyString(ses: Set[ShadowedEndpoint]): String =
    ses.map(_.toString()).mkString("\n")
}
