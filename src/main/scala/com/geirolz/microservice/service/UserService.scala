package com.geirolz.microservice.service

import cats.effect.IO
import com.geirolz.microservice.external.repository.UserRepository
import com.geirolz.microservice.model.User
import com.geirolz.microservice.model.values.UserId

trait UserService {
  def getById(id: UserId): IO[Option[User]]
}
object UserService {

  def apply(userRepository: UserRepository): UserService = new UserService {
    override def getById(id: UserId): IO[Option[User]] =
      userRepository.getById(id)
  }
}
