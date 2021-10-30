package com.geirolz.microservice.common.data

trait Scope

sealed class ScopeContext private[data] () {
  type ScopeType <: Scope
}
sealed class TypedScopeContext[S <: Scope] private[data] () extends ScopeContext() {
  type ScopeType = S
}
object ScopeContext {

  private val genericScopeContext: TypedScopeContext[Scope] = new TypedScopeContext[Scope]

  def of[S <: Scope]: TypedScopeContext[S] = genericScopeContext.asInstanceOf[TypedScopeContext[S]]
}

object Scope {
  trait Domain extends Scope
  trait Persistence extends Scope
  trait Endpoint extends Scope
  trait Queue extends Scope
}
