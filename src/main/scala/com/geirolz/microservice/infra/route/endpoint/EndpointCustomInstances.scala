package com.geirolz.microservice.infra.route.endpoint

import com.geirolz.microservice.model.datatype.UserId
import io.circe.{Decoder, Encoder}
import shapeless.Unwrapped
import sttp.tapir.CodecFormat.TextPlain
import sttp.tapir.Validator
import sttp.tapir.integ.cats.TapirCodecCats

private[endpoint] object EndpointCustomInstances
    extends EndpointCustomCodecsInstances
    with EndpointCustomSchemasInstances
    with EndpointCustomValidators
    with EndpointCustomStatusMapping {
  object Codecs extends EndpointCustomCodecsInstances
  object Schemas extends EndpointCustomSchemasInstances
  object Validators extends EndpointCustomValidators
  object StatusMapping extends EndpointCustomStatusMapping
}

sealed private trait EndpointCustomCodecsInstances extends TapirCodecCats {

  import sttp.tapir._

  //circe
  implicit def decodeAnyVal[T, U](implicit
    ev: T <:< AnyVal,
    unwrapped: Unwrapped.Aux[T, U],
    decoder: Decoder[U]
  ): Decoder[T] = Decoder.instance[T] { cursor =>
    decoder(cursor).map(value => unwrapped.wrap(value))
  }

  implicit def encodeAnyVal[T, U](implicit
    ev: T <:< AnyVal,
    unwrapped: Unwrapped.Aux[T, U],
    encoder: Encoder[U]
  ): Encoder[T] = Encoder.instance[T] { value =>
    encoder(unwrapped.unwrap(value))
  }

  //tapir
  implicit val codecForUserId: Codec[String, UserId, TextPlain] = Codec.long.map(UserId)(_.value)
}

sealed private trait EndpointCustomSchemasInstances

sealed private trait EndpointCustomValidators {

  def rangeValidator[N: Numeric](
    min: N,
    max: N,
    minExclusive: Boolean = false,
    maxExclusive: Boolean = false
  ): Validator[N] =
    Validator.min[N](min, minExclusive).and(Validator.max[N](max, maxExclusive))
}

sealed private trait EndpointCustomStatusMapping {}
