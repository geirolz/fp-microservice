package com.geirolz.microservice.infra.route.endpoint

import cats.data.NonEmptyList
import com.geirolz.microservice.model.datatype.UserId
import sttp.tapir.CodecFormat.TextPlain
import sttp.tapir.Validator

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

sealed private trait EndpointCustomCodecsInstances {

  import sttp.tapir._

  implicit val codecForUserId: Codec[String, UserId, TextPlain] = Codec.long.map(UserId)(_.value)
}

sealed private trait EndpointCustomSchemasInstances {

  import sttp.tapir._

  implicit def customSchemaForNonEmptyList[T: Schema]: Schema[NonEmptyList[T]] =
    Schema(
      schemaType = SchemaType.SArray(implicitly[Schema[T]])(_.toList),
      isOptional = false,
      validator = Validator.minSize(1).contramap(_.toList)
    )

  implicit val customSchemaForBigDecimal: Schema[BigDecimal] =
    Schema.schemaForBigDecimal.copy(
      schemaType = SchemaType.SNumber[BigDecimal]()
    )
}

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
