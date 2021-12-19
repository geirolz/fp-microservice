package com.geirolz.microservice.route

import cats.effect.IO
import sttp.tapir.server.http4s.Http4sServerOptions
import sttp.tapir.server.interceptor.decodefailure.DefaultDecodeFailureHandler
import sttp.tapir.server.interceptor.exception.DefaultExceptionHandler
import sttp.tapir.server.interceptor.reject.RejectInterceptor

object ServerConfiguration {

  val options: Http4sServerOptions[IO, IO] = Http4sServerOptions
    .customInterceptors[IO, IO]
    .rejectInterceptor(RejectInterceptor.default[IO])
    .decodeFailureHandler(DefaultDecodeFailureHandler.default)
    .exceptionHandler(DefaultExceptionHandler.handler)
    .serverLog(Http4sServerOptions.Log.defaultServerLog[IO])
    .options
}