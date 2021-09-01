package com.geirolz.microservice.common.data

import cats.{Applicative, FlatMap, Functor, Id}
import cats.effect.{IO, LiftIO}
import com.geirolz.microservice.common.data.ModelMapper.{ModelMapperIO, ModelMapperId}

import scala.annotation.{implicitAmbiguous, implicitNotFound}

trait Scope
object Scope {
  sealed trait Domain extends Scope
  sealed trait Persistence extends Scope
  sealed trait Endpoint extends Scope
  sealed trait Queue extends Scope
  sealed trait EventIn extends Scope
  sealed trait EventOut extends Scope
  sealed trait ReadOp extends Scope
  sealed trait WriteOp extends Scope
}

@implicitNotFound(msg = "Cannot find a mapper for the scope ${S}")
@implicitAmbiguous(msg = "Multiple mapper for the same type ${M2} and same scope ${S}")
final class ModelMapper[F[_], S <: Scope, A, B] private (mapper: A => F[B]) {

  import cats.implicits.*

  type TargetType = B

  def apply(a: A): F[TargetType] = mapper(a)

  def map[C](f: B => C)(implicit F: Functor[F]): ModelMapper[F, S, A, C] =
    ModelMapper(mapper.andThen(_.map(f)))

  def flatMap[C](f: B => F[C])(implicit F: FlatMap[F]): ModelMapper[F, S, A, C] =
    ModelMapper(mapper.andThen(_.flatMap(f)))
}
object ModelMapper extends ModelScopeMapperSyntax {

  type ModelMapperId[S <: Scope, A, B] = ModelMapper[Id, S, A, B]
  type ModelMapperIO[S <: Scope, A, B] = ModelMapper[IO, S, A, B]

  /** Lift a plain function `A => B` into `A => Id[B]` to create a [[ModelMapper]] instance
    * @param f
    *   Function from `A => B`
    * @tparam S
    *   The scope where this function exists
    * @tparam A
    *   Input type
    * @tparam B
    *   Non-Monoidal Output type
    * @return
    *   ModelMapper instance based on `Id` valid for the selected scope
    */
  def lift[S <: Scope, A, B](f: A => B): ModelMapperId[S, A, B] = new ModelMapper[Id, S, A, B](f)

  def apply[F[_], S <: Scope, A, B](f: A => F[B]): ModelMapper[F, S, A, B] =
    new ModelMapper[F, S, A, B](f)

  def pure[F[_]: Applicative, S <: Scope, A, B](b: B): ModelMapper[F, S, A, B] =
    apply(_ => Applicative[F].pure(b))

  def id[S <: Scope, A]: ModelMapperId[S, A, A] = ModelMapper[Id, S, A, A](a => a)
}

sealed trait ModelScopeMapperSyntax {
  implicit class ModelScopeMapperSyntaxOps[A](a: A) {
    def toScope[F[_], S <: Scope](implicit m: ModelMapper[F, S, A, ?]): F[m.TargetType] = m(a)
    def toScopeId[S <: Scope](implicit m: ModelMapperId[S, A, ?]): m.TargetType = m(a)
    def toScopeIO[S <: Scope](implicit m: ModelMapperIO[S, A, ?]): IO[m.TargetType] = m(a)
    def toScopeIOLifted[F[_]: LiftIO, S <: Scope](implicit
      m: ModelMapperIO[S, A, ?]
    ): F[m.TargetType] =
      LiftIO[F].liftIO(m(a))
  }
}
