package com.geirolz.microservice.common.logging

import cats.arrow.FunctionK
import cats.effect.{IO, Resource}
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object FLog {

  trait FLogSupport {
    def logger[F[_]: SelfAwareStructuredLogger]: SelfAwareStructuredLogger[F] = implicitly[SelfAwareStructuredLogger[F]]
  }

  trait IOLog extends FLogSupport {
    implicit protected val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]
  }

  trait IOResourceLog extends FLogSupport { this: IOLog =>
    implicit protected val resLogger: SelfAwareStructuredLogger[Resource[IO, *]] =
      logger.mapK(new FunctionK[IO, Resource[IO, *]] {
        override def apply[A](fa: IO[A]): Resource[IO, A] = Resource.eval(fa)
      })
  }
}
