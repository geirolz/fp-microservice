package com.geirolz.microservice.route.endpoint

import sttp.tapir.Validator

private[endpoint] object EndpointCustomInstances
    extends EndpointCustomValidators
    with EndpointCustomStatusMapping {
  object Validators extends EndpointCustomValidators
  object StatusMapping extends EndpointCustomStatusMapping
}

sealed trait EndpointCustomValidators {

  def rangeValidator[N: Numeric](
    min: N,
    max: N,
    minExclusive: Boolean = false,
    maxExclusive: Boolean = false
  ): Validator[N] =
    Validator.min[N](min, minExclusive).and(Validator.max[N](max, maxExclusive))
}

sealed trait EndpointCustomStatusMapping {}
