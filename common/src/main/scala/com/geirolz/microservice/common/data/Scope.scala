package com.geirolz.microservice.common.data

trait Scope
object Scope {
  trait Domain extends Scope
  trait Persistence extends Scope
  trait PersistenceRead extends Scope
  trait PersistenceWrite extends Scope
  trait Endpoint extends Scope
  trait Event extends Scope
  trait Command extends Scope
  trait Read extends Scope
  trait Write extends Scope
}

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
