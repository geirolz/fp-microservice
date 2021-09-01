package com.geirolz.microservice.common.data

import cats.{~>, Applicative, FlatMap, Functor, Id}
import cats.effect.{IO, LiftIO}
import com.geirolz.microservice.common.data.ModelMapper.ModelMapperIO

import scala.annotation.{implicitAmbiguous, implicitNotFound}

trait Scope
object Scope {

  def ctx[S <: Scope]: ScopeContext[S] = new ScopeContext[S]

  sealed trait Domain extends Scope
  sealed trait Persistence extends Scope
  sealed trait Endpoint extends Scope
  sealed trait Queue extends Scope
}

sealed class ScopeContext[S <: Scope] {
  type ScopeType = S
}

@implicitNotFound(msg = "Cannot find a mapper for the scope ${S}")
@implicitAmbiguous(msg = "Multiple mapper for the same type ${M2} and same scope ${S}")
final class ModelMapper[F[_], S <: Scope, A, B] private (mapper: A => F[B]) {

  import cats.implicits._

  type TargetType = B

  def apply(a: A)(implicit scopeContext: ScopeContext[S]): F[TargetType] = mapper(a)

  def map[C](f: B => C)(implicit F: Functor[F]): ModelMapper[F, S, A, C] =
    ModelMapper.of(mapper.andThen(_.map(f)))

  def mapK[K[_]](f: F ~> K): ModelMapper[K, S, A, B] =
    ModelMapper.of(mapper.andThen(f(_)))

  def flatMap[C](f: B => F[C])(implicit F: FlatMap[F]): ModelMapper[F, S, A, C] =
    ModelMapper.of(mapper.andThen(_.flatMap(f)))
}
object ModelMapper extends ModelScopeMapperSyntax {

  type ModelMapperId[S <: Scope, A, B] = ModelMapper[Id, S, A, B]
  type ModelMapperIO[S <: Scope, A, B] = ModelMapper[IO, S, A, B]

  /** Summoner smart constructor
    */
  def apply[F[_], S <: Scope, A, B](implicit m: ModelMapper[F, S, A, B]): ModelMapper[F, S, A, B] = m

  /** Lift a plain function `A => B` into `A => Id[B]` to create a [[ModelMapper]] instance
    * @param f Function from `A => B`
    * @tparam S The scope where this function exists
    * @tparam A Input type
    * @tparam B Non-Monoidal Output type
    * @return ModelMapper instance based on `Id` valid for the selected scope
    */
  def lift[F[_]: Applicative, S <: Scope, A, B](f: A => B): ModelMapper[F, S, A, B] =
    ModelMapper.of(f.andThen(Applicative[F].pure(_)))

  /** Create a new ModelMapper
    */
  def of[F[_], S <: Scope, A, B](f: A => F[B]): ModelMapper[F, S, A, B] = new ModelMapper[F, S, A, B](f)

  def pure[F[_]: Applicative, S <: Scope, A, B](b: B): ModelMapper[F, S, A, B] = of(_ => Applicative[F].pure(b))

  def id[S <: Scope, A]: ModelMapperId[S, A, A] = ModelMapper.of(a => a)
}

sealed trait ModelScopeMapperSyntax {
//
//  implicit def liftMapperIdApplicative[K[_]: Applicative, S <: Scope, A, B](implicit
//    mapperId: ModelMapperId[S, A, B]
//  ): ModelMapper[K, S, A, B] =
//    mapperId.mapK(new (Id ~> K) {
//      override def apply[AA](fa: Id[AA]): K[AA] = Applicative[K].pure(fa)
//    })

  implicit def liftMapperIO[K[_]: LiftIO, S <: Scope, A, B](implicit
    mapperIO: ModelMapperIO[S, A, B]
  ): ModelMapper[K, S, A, B] =
    mapperIO.mapK(new (IO ~> K) {
      override def apply[AA](ioa: IO[AA]): K[AA] = LiftIO[K].liftIO(ioa)
    })

  implicit class ModelScopeMapperSyntaxOps[A](a: A) {
    implicit def scope[S <: Scope: ScopeContext]: ScopeOpsForAny[A, S] = new ScopeOpsForAny[A, S](a)
  }

  sealed class ScopeOpsForAny[A, S <: Scope: ScopeContext] private[data] (a: A) {
    def map[F[_]](implicit m: ModelMapper[F, S, A, _]): F[m.TargetType] = m(a)
  }
}
