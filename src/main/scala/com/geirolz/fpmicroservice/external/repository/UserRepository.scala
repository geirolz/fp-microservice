package com.geirolz.fpmicroservice.external.repository

import cats.effect.IO
import com.geirolz.fpmicroservice.external.repository.entity.UserEntity
import com.geirolz.fpmicroservice.model.User
import com.geirolz.fpmicroservice.model.values.UserId
import doobie.ConnectionIO
import doobie.implicits.*
import doobie.util.transactor.Transactor
import scope.{InScope, Scope, ScopeContext, TypedScopeContext}

trait UserRepository {
  def getById(id: UserId): IO[Option[User]]
}
object UserRepository extends InScope[Scope.Persistence] {

  import cats.implicits.*
  import scope.syntax.*

  def apply(dbTransactor: Transactor[IO]): UserRepository = new UserRepository {

    def getById(id: UserId): IO[Option[User]] =
      Queries
        .findUserById(id)
        .option
        .transact(dbTransactor)
        .nested
        .map(_.scoped.as[User])
        .value

  }

  private[repository] object Queries {

    def findUserById(userId: UserId): doobie.Query0[UserEntity.Read] =
      sql"""SELECT * FROM "user" WHERE id = ${userId.value}"""
        .query[UserEntity.Read]
  }
}
