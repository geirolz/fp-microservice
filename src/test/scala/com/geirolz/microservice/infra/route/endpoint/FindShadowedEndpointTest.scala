package com.geirolz.microservice.infra.route.endpoint

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import sttp.tapir.testing.{FindShadowedEndpoints, ShadowedEndpoint}
import sttp.tapir.Endpoint

class FindShadowedEndpointTest extends AnyWordSpec with Matchers {

  "Application endpoints" should {
    "not have shadowing" in {
      findShadowedEndpoints(EndpointsApi.all).left
        .map(ShadowedEndpointException)
        .toTry
        .get
    }
  }

  case class ShadowedEndpointException(ses: Set[ShadowedEndpoint])
      extends RuntimeException(s"${ses.size} endpoints shadowed.\n${prettyStringShadowedEndpoint(ses)}")

  //TODO: Open a PR to tapir
  def findShadowedEndpoints(endpoints: List[Endpoint[_, _, _, _]]): Either[Set[ShadowedEndpoint], Unit] = {
    FindShadowedEndpoints(endpoints) match {
      case ses if ses.isEmpty => Right(())
      case ses                => Left(ses)
    }
  }
  def prettyStringShadowedEndpoint(ses: Set[ShadowedEndpoint]): String =
    ses.map(_.toString()).mkString("\n")

}
