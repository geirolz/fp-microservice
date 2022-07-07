package com.geirolz.fpmicroservice.http.route

import cats.{Semigroup, SemigroupK}
import org.http4s.HttpRoutes
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter

class ServerRoutes[
  F[_],
  ROUTES,
  -R
](
  val endpoints: List[ServerEndpoint[R, F]],
  val interpretedRoutes: ROUTES
)
object ServerRoutes extends ServerRoutesInstances {

  implicit class ServerRoutesOps[SE <: ServerEndpoint[R, F], R, F[_]](serverEndpoint: SE) {
    def interpret[ROUTES](f: SE => ROUTES): ServerRoutes[F, ROUTES, R] =
      new ServerRoutes(
        List(serverEndpoint),
        f(serverEndpoint)
      )
  }

  // http4s
  type Http4sServerRoutes[F[_]] = ServerRoutes[F, HttpRoutes[F], Fs2Streams[F]]

  implicit class TapirCompiledRouteHttp4sOps[F[_]](
    serverEndpoint: ServerEndpoint[Fs2Streams[F], F]
  ) {
    def toRoutes(
      interpreter: Http4sServerInterpreter[F]
    ): Http4sServerRoutes[F] =
      new ServerRoutes(
        List(serverEndpoint),
        interpreter.toRoutes(serverEndpoint)
      )
  }
}
sealed trait ServerRoutesInstances {

  import cats.implicits.*

  type AnyF[_] = Any

  implicit def serverRoutesSemigroup[F[_], ROUTES, R](implicit
    semigroup: Semigroup[ROUTES]
  ): Semigroup[ServerRoutes[F, ROUTES, R]] =
    (x, y) =>
      new ServerRoutes[F, ROUTES, R](
        x.endpoints ++ y.endpoints,
        x.interpretedRoutes |+| y.interpretedRoutes
      )

  implicit def serverRoutesSemigroupK[F[_], ROUTES: Semigroup, *]
    : SemigroupK[ServerRoutes[F, ROUTES, *]] =
    new SemigroupK[ServerRoutes[F, ROUTES, *]] {
      override def combineK[R](
        x: ServerRoutes[F, ROUTES, R],
        y: ServerRoutes[F, ROUTES, R]
      ): ServerRoutes[F, ROUTES, R] = {

        new ServerRoutes[F, ROUTES, R](
          x.endpoints ++ y.endpoints,
          x.interpretedRoutes |+| y.interpretedRoutes
        )
      }
    }

  implicit def serverRoutesSemigroupKForKTypes[F[_], ROUTES[A] <: AnyF[F[A]], T](implicit
    cc: SemigroupK[ROUTES]
  ): SemigroupK[ServerRoutes[F, ROUTES[T], *]] =
    new SemigroupK[ServerRoutes[F, ROUTES[T], *]] {
      override def combineK[R](
        x: ServerRoutes[F, ROUTES[T], R],
        y: ServerRoutes[F, ROUTES[T], R]
      ): ServerRoutes[F, ROUTES[T], R] =
        new ServerRoutes[F, ROUTES[T], R](
          x.endpoints ++ y.endpoints,
          SemigroupK[ROUTES].combineK[T](x.interpretedRoutes, y.interpretedRoutes)
        )
    }
}
