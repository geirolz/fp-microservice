package com.geirolz.microservice.external.repository

import cats.effect.IO
import com.geirolz.microservice.external.repository.entity.UserEntity
import com.geirolz.microservice.model.User
import com.geirolz.microservice.model.values.UserId
import doobie.ConnectionIO
import doobie.implicits.*
import doobie.util.transactor.Transactor
import scope.{Scope, ScopeContext, TypedScopeContext}

trait UserRepository {
  def getById(id: UserId): IO[Option[User]]
}

object UserRepository {

  import cats.implicits.*
  import scope.syntax.*

  implicit protected val scopeCtx: TypedScopeContext[Scope.Persistence] =
    ScopeContext.of[Scope.Persistence]

  def apply(dbTransactor: Transactor[IO]): UserRepository = new UserRepository {

    def getById(id: UserId): IO[Option[User]] =
      Query
        .findUserById(id)
        .option
        .transact(dbTransactor)
        .nested
        .map(_.scoped.as[User])
        .value

  }

  private[repository] object Query {

    def findUserById(userId: UserId): doobie.Query0[UserEntity.Read] =
      sql"SELECT * FROM user WHERE id == ${userId.value}"
        .query[UserEntity.Read]
  }
}
