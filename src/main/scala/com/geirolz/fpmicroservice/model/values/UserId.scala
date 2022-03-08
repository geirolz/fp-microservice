package com.geirolz.fpmicroservice.model.values

import sttp.tapir.Codec
import sttp.tapir.Codec.PlainCodec
import sttp.tapir.CodecFormat.TextPlain

case class UserId(value: Long) extends AnyVal
object UserId {
  implicit val tapirCodecForUserId: PlainCodec[UserId] =
    Codec.long.map(UserId(_))(_.value)
}
