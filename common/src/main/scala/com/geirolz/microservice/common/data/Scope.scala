package com.geirolz.microservice.common.data

import cats.{FlatMap, Functor}

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
  def apply[F[_], S <: Scope, A, B](f: A => F[B]): ModelScopeMapper[F, S, A, B] = new ModelScopeMapper[F, S, A, B](f)
}

sealed trait ModelScopeMapperSyntax {
  implicit class TypeScopeMapperSyntaxOps[A](a: A) {
    def toScope[F[_], S <: Scope](implicit m: ModelScopeMapper[F, S, A, _]): F[m.TargetType] = m(a)
  }
}
