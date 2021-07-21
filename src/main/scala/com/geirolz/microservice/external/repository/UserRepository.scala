package com.geirolz.microservice.external.repository

import cats.effect.IO
import com.geirolz.microservice.external.repository.entity.UserEntity
import com.geirolz.microservice.model.User
import com.geirolz.microservice.model.datatype.UserId
import doobie.ConnectionIO
import doobie.implicits._
import doobie.util.transactor.Transactor

trait UserRepository {
  def getById(id: UserId): IO[Option[User]]
}
object UserRepository {

  //TODO: TBD
  def apply(dbTransactor: Transactor[IO]): UserRepository = new UserRepository {

    import cats.implicits._

    def getById(id: UserId): IO[Option[User]] =
      Query
        .findUserById(id)
        .transact(dbTransactor)
        .nested
        .map(_.toDomain)
        .value

  }

  private[repository] object Query {

    def findUserById(userId: UserId): ConnectionIO[Option[UserEntity]] =
      sql"SELECT * FROM user WHERE id == ${userId.value}"
        .query[UserEntity]
        .option
  }
}
