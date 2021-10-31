package com.geirolz.microservice.route.endpoint

private[endpoint] object EndpointCustomInstances
    extends EndpointCustomValidators
    with EndpointCustomStatusMapping {
  object Validators extends EndpointCustomValidators
  object StatusMapping extends EndpointCustomStatusMapping
}

sealed trait EndpointCustomValidators
sealed trait EndpointCustomStatusMapping
