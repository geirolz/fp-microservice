package com.geirolz.microservice.external.repository

import cats.effect.IO
import com.geirolz.microservice.external.repository.entity.UserEntity
import com.geirolz.microservice.external.repository.UserRepository.Query
import com.geirolz.microservice.model.User
import com.geirolz.microservice.model.datatype.UserId
import doobie.ConnectionIO
import doobie.implicits._
import doobie.util.transactor.Transactor

class UserRepository(dbTransactor: Transactor[IO]) {

  import cats.implicits._

  def getById(id: UserId): IO[Option[User]] =
    Query
      .findUserById(id)
      .transact(dbTransactor)
      .nested
      .map(_.toDomain)
      .value

}
object UserRepository {

  private[repository] object Query {

    def findUserById(userId: UserId): ConnectionIO[Option[UserEntity]] =
      sql"SELECT * FROM user WHERE id == ${userId.value}"
        .query[UserEntity]
        .option
  }
}
