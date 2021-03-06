package com.geirolz.fpmicroservice.external.repository.entity

import com.geirolz.fpmicroservice.model.User
import com.geirolz.fpmicroservice.model.values.{Email, FirstName, LastName, MiddleName, UserId}
import scope.{ModelMapper, Scope}

object UserEntity {

  case class Write(
    id: UserId,
    email: Email,
    firstName: FirstName,
    middleName: Option[MiddleName],
    lastName: LastName
  )

  case class Read(
    id: UserId,
    email: Email,
    firstName: FirstName,
    middleName: Option[MiddleName],
    lastName: LastName
  )
  object Read {
    implicit val scopePersistenceMapper: ModelMapper[Scope.Persistence, UserEntity.Read, User] =
      ModelMapper.scoped { entity =>
        User(
          id         = entity.id,
          email      = entity.email,
          firstName  = entity.firstName,
          middleName = entity.middleName,
          lastName   = entity.lastName
        )
      }
  }

}
