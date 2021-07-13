package com.geirolz.microservice.common.data

import cats.{FlatMap, Functor, Id}
import cats.effect.IO
import com.geirolz.microservice.common.data.ModelScopeMapper.{ModelScopeMapperIO, ModelScopeMapperId}

import scala.annotation.{implicitAmbiguous, implicitNotFound}

trait Scope
sealed trait Domain extends Scope
sealed trait Persistence extends Scope
sealed trait Endpoint extends Scope
sealed trait Queue extends Scope

@implicitNotFound(msg = "Cannot find a mapper for the scope ${S}")
@implicitAmbiguous(msg = "Multiple mapper for the same type ${M2} and same scope ${S}")
final class ModelScopeMapper[F[_], S <: Scope, A, B] private (mapper: A => F[B]) {

  import cats.implicits._

  type TargetType = B

  def apply(a: A): F[TargetType] = mapper(a)

  def map[C](f: B => C)(implicit F: Functor[F]): ModelScopeMapper[F, S, A, C] =
    ModelScopeMapper(mapper.andThen(_.map(f)))

  def flatMap[C](f: B => F[C])(implicit F: FlatMap[F]): ModelScopeMapper[F, S, A, C] =
    ModelScopeMapper(mapper.andThen(_.flatMap(f)))
}
object ModelScopeMapper extends ModelScopeMapperSyntax {

  type ModelScopeMapperId[S <: Scope, A, B] = ModelScopeMapper[Id, S, A, B]
  type ModelScopeMapperIO[S <: Scope, A, B] = ModelScopeMapper[IO, S, A, B]

  def apply[F[_], S <: Scope, A, B](f: A => F[B]): ModelScopeMapper[F, S, A, B] = new ModelScopeMapper[F, S, A, B](f)

  def id[S <: Scope, A, B](f: A => B): ModelScopeMapperId[S, A, B] = ModelScopeMapper[Id, S, A, B](f)
}

sealed trait ModelScopeMapperSyntax {
  implicit class ModelScopeMapperSyntaxOps[A](a: A) {
    def toScope[F[_], S <: Scope](implicit m: ModelScopeMapper[F, S, A, _]): F[m.TargetType] = m(a)
    def toScopeId[S <: Scope](implicit m: ModelScopeMapperId[S, A, _]): m.TargetType = m(a)
    def toScopeIO[S <: Scope](implicit m: ModelScopeMapperIO[S, A, _]): IO[m.TargetType] = m(a)
  }
}
