package com.geirolz.microservice.model.values

import sttp.tapir.Codec
import sttp.tapir.CodecFormat.TextPlain

case class UserId(value: Long) extends AnyVal
object UserId {
  implicit val tapirCodecForUserId: Codec[String, UserId, TextPlain] =
    Codec.long.map(UserId(_))(_.value)
}
