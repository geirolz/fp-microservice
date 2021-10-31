package com.geirolz.microservice.common.data

import cats.{~>, Applicative, FlatMap, Functor}
import cats.effect.{IO, LiftIO}
import cats.implicits.*

import scala.annotation.{implicitAmbiguous, implicitNotFound}

@implicitNotFound(msg = "Cannot find a mapper for the scope ${S}")
@implicitAmbiguous(msg = "Multiple mapper for the same type ${M2} and same scope ${S}")
class ModelMapperK[F[_], S <: Scope, A, B](mapper: A => F[B]) {

  type TargetType = B

  def apply(a: A)(implicit scopeContext: TypedScopeContext[S]): F[TargetType] = mapper(a)

  def map[C](f: B => C)(implicit F: Functor[F]): ModelMapperK[F, S, A, C] =
    ModelMapperK.scope[S](mapper.andThen(_.map(f)))

  def mapK[K[_]](f: F ~> K): ModelMapperK[K, S, A, B] =
    ModelMapperK.scope[S](mapper.andThen(f(_)))

  def flatMap[C](f: B => F[C])(implicit F: FlatMap[F]): ModelMapperK[F, S, A, C] =
    ModelMapperK.scope[S](mapper.andThen(_.flatMap(f)))
}
object ModelMapperK extends ModelScopeMapperKSyntax {

  private val builderK: ModelMapperK.Builder[Scope] = new ModelMapperK.Builder[Scope]

  def scope[S <: Scope]: ModelMapperK.Builder[S] =
    builderK.asInstanceOf[ModelMapperK.Builder[S]]

  class Builder[S <: Scope] private[ModelMapperK] () {

    def summon[F[_], A, B](implicit m: ModelMapperK[F, S, A, B]): ModelMapperK[F, S, A, B] = m

    def apply[F[_], A, B](f: A => F[B]): ModelMapperK[F, S, A, B] =
      new ModelMapperK[F, S, A, B](f)

    def pure[F[_]: Applicative, A, B](b: B): ModelMapperK[F, S, A, B] =
      apply(_ => Applicative[F].pure(b))

    def id[F[_]: Applicative, A]: ModelMapperK[F, S, A, A] =
      apply(Applicative[F].pure)

    /** Lift a plain function `A => B` into `A => Id[B]` to create a [[ModelMapperK]] instance
      *
      * @param f
      *   Function from `A => B`
      * @tparam A
      *   Input type
      * @tparam B
      *   Non-Monoidal Output type
      * @return
      *   ModelMapper instance based on `Id` valid for the selected scope
      */
    def lift[F[_]: Applicative, A, B](f: A => B): ModelMapperK[F, S, A, B] =
      apply(f.andThen(Applicative[F].pure(_)))
  }
}

private[data] trait ModelScopeMapperKSyntax {

  implicit def liftMapperIO[K[_]: LiftIO, S <: Scope, A, B](implicit
    mapperIO: ModelMapperK[IO, S, A, B]
  ): ModelMapperK[K, S, A, B] =
    mapperIO.mapK(new (IO ~> K) {
      override def apply[AA](ioa: IO[AA]): K[AA] = LiftIO[K].liftIO(ioa)
    })

  implicit class ModelScopeMapperKSyntaxOps[A](a: A) {
    def inScope(implicit ctx: ScopeContext): ScopeOpsForAny[A, ctx.ScopeType] =
      new ScopeOpsForAny[A, ctx.ScopeType](a)
  }

  sealed class ScopeOpsForAny[A, S <: Scope] private[data] (a: A) {

    implicit def as[B](implicit m: ModelMapper[S, A, B], tsc: TypedScopeContext[S]): B = m.apply(a)

    implicit def as[F[_], B](implicit
      m: ModelMapperK[F, S, A, B],
      tsc: TypedScopeContext[S]
    ): F[B] = m.apply(a)
  }
}
