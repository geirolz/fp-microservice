package com.geirolz.microservice.route.endpoint.user

import com.geirolz.microservice.model.value.UserId
import com.geirolz.microservice.route.endpoint.{EndpointStandardCodecs, VersionedEndpoint}
import com.geirolz.microservice.route.endpoint.user.contract.UserContract
import com.geirolz.microservice.route.endpoint.user.UserEndpointApi.Errors.ErrorInfo

private[route] object UserEndpointApi {

  import io.circe.generic.auto._
  import sttp.tapir._
  import sttp.tapir.generic.auto._
  import sttp.tapir.json.circe._
  import EndpointStandardCodecs._

  private val user: Endpoint[Unit, ErrorInfo, Unit, Any] =
    VersionedEndpoint.v1.in("user").errorOut(jsonBody[ErrorInfo])

  val getById: Endpoint[UserId, ErrorInfo, UserContract, Any] =
    user.get
      .in(query[UserId]("id"))
      .out(jsonBody[UserContract])

  object Errors {
    sealed trait ErrorInfo
    case class UserNotFound(userId: UserId) extends ErrorInfo
  }
}
