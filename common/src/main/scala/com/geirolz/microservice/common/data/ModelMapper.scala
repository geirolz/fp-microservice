package com.geirolz.microservice.common.data

import cats.{Applicative, FlatMap, Functor, Id}
import cats.effect.IO
import com.geirolz.microservice.common.data.ModelMapper.{ModelMapperIO, ModelMapperId}

import scala.annotation.{implicitAmbiguous, implicitNotFound}

trait Scope
sealed trait Domain extends Scope
sealed trait Persistence extends Scope
sealed trait Endpoint extends Scope
sealed trait Queue extends Scope

@implicitNotFound(msg = "Cannot find a mapper for the scope ${S}")
@implicitAmbiguous(msg = "Multiple mapper for the same type ${M2} and same scope ${S}")
final class ModelMapper[F[_], S <: Scope, A, B] private (mapper: A => F[B]) {

  import cats.implicits._

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

  def apply[F[_], S <: Scope, A, B](f: A => F[B]): ModelMapper[F, S, A, B] = new ModelMapper[F, S, A, B](f)

  def pure[F[_]: Applicative, S <: Scope, A, B](b: => B): ModelMapper[F, S, A, B] = apply(_ => Applicative[F].pure(b))

  def id[S <: Scope, A, B](f: A => B): ModelMapperId[S, A, B] = ModelMapper[Id, S, A, B](f)
}

sealed trait ModelScopeMapperSyntax {
  implicit class ModelScopeMapperSyntaxOps[A](a: A) {
    def inScope[F[_], S <: Scope](implicit m: ModelMapper[F, S, A, _]): F[m.TargetType] = m(a)
    def inScopeId[S <: Scope](implicit m: ModelMapperId[S, A, _]): m.TargetType = m(a)
    def inScopeIO[S <: Scope](implicit m: ModelMapperIO[S, A, _]): IO[m.TargetType] = m(a)
  }
}
