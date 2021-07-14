package com.geirolz.microservice.infra.route.endpoint.util

import com.geirolz.microservice.model.datatype.UserId
import sttp.tapir.CodecFormat.TextPlain

private[endpoint] object EndpointCommonCodecs {

  import sttp.tapir._

  implicit val codecForUserId: Codec[String, UserId, TextPlain] = Codec.long.map(UserId)(_.value)
}
