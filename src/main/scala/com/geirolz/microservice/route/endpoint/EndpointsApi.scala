package com.geirolz.microservice.route.endpoint

import com.geirolz.microservice.model.value.UserId
import sttp.tapir.CodecFormat.TextPlain

private[endpoint] trait EndpointsApi extends EndpointVersions with EndpointStandardCodecs

private[endpoint] trait EndpointVersions {
  import sttp.tapir._

  val v1: Endpoint[Unit, Unit, Unit, Any] = v(1)

  private def v(version: Int): Endpoint[Unit, Unit, Unit, Any] = endpoint.in("api" / s"v$version")
}

private[endpoint] trait EndpointStandardCodecs {
  import sttp.tapir._
  import sttp.tapir.generic.auto._

  //TODO: how to improve ?
  implicit def codecForUserId[T]: Codec[String, UserId, TextPlain] = Codec.stringCodec(str => UserId(str.toLong))
}
