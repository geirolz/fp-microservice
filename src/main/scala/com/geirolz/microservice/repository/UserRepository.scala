package com.geirolz.microservice.repository

import cats.effect.IO
import com.geirolz.microservice.model.value.UserId
import com.geirolz.microservice.repository.entity.UserEntity
import doobie.ConnectionIO
import doobie.util.transactor.Transactor

class UserRepository(dbTransactor: Transactor[IO]) {

  import doobie.implicits._

  def getById(id: UserId): IO[Option[UserEntity]] =
    SQL
      .findUserById(id)
      .transact(dbTransactor)

  private object SQL {

    val findUserById: UserId => ConnectionIO[Option[UserEntity]] = userId =>
      sql"SELECT * FROM user WHERE id == ${userId.value}"
        .query[UserEntity]
        .option
  }
}
