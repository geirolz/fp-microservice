package com.geirolz.microservice.infra.route.endpoint.util

import com.geirolz.microservice.model.value.UserId
import sttp.tapir.CodecFormat.TextPlain

private[endpoint] object EndpointCommonCodecs {

  import sttp.tapir._

  implicit def codecForUserId[T]: Codec[String, UserId, TextPlain] = Codec.long.map(UserId)(_.value)
}
