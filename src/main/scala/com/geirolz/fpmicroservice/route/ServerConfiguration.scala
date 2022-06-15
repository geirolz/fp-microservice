package com.geirolz.fpmicroservice.route

import cats.effect.IO
import sttp.tapir.server.http4s.Http4sServerOptions
import sttp.tapir.server.interceptor.decodefailure.DefaultDecodeFailureHandler
import sttp.tapir.server.interceptor.exception.DefaultExceptionHandler
import sttp.tapir.server.interceptor.reject.DefaultRejectHandler

object ServerConfiguration {

  val options: Http4sServerOptions[IO] =
    Http4sServerOptions
      .customiseInterceptors[IO]
      .rejectHandler(DefaultRejectHandler[IO])
      .decodeFailureHandler(DefaultDecodeFailureHandler.default)
      .exceptionHandler(DefaultExceptionHandler[IO])
      .serverLog(Http4sServerOptions.defaultServerLog[IO])
      .options
}
