package com.geirolz.fpmicroservice.service

import cats.effect.IO
import com.geirolz.fpmicroservice.external.repository.UserRepository
import com.geirolz.fpmicroservice.model.User
import com.geirolz.fpmicroservice.model.values.UserId

trait UserService {
  def getById(id: UserId): IO[Option[User]]
}
object UserService {

  def apply(userRepository: UserRepository): UserService = new UserService {
    override def getById(id: UserId): IO[Option[User]] =
      userRepository.getById(id)
  }
}
