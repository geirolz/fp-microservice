package com.geirolz.microservice.common.data

import cats.Id

import scala.annotation.{implicitAmbiguous, implicitNotFound}

@implicitNotFound(msg = "Cannot find a mapper for the scope ${S}")
@implicitAmbiguous(msg = "Multiple mapper for the same type ${M2} and same scope ${S}")
class ModelMapper[S <: Scope, A, B](mapper: A => B) extends ModelMapperK[Id, S, A, B](mapper)
object ModelMapper extends ModelScopeMapperKSyntax {

  private val builder: ModelMapper.Builder[Scope] = new ModelMapper.Builder[Scope]

  def forScope[S <: Scope]: ModelMapper.Builder[S] =
    builder.asInstanceOf[ModelMapper.Builder[S]]

  class Builder[S <: Scope] private[ModelMapper] () {

    def summon[A, B](implicit m: ModelMapper[S, A, B]): ModelMapper[S, A, B] = m

    def apply[A, B](f: A => B): ModelMapper[S, A, B] = new ModelMapper(f)

    def pure[A, B](b: B): ModelMapper[S, A, B] = apply(_ => b)

    def id[A]: ModelMapper[S, A, A] = apply(a => a)
  }
}
