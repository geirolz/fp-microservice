package com.geirolz.microservice.service

import cats.effect.IO
import com.geirolz.microservice.model.User
import com.geirolz.microservice.model.value.UserId
import com.geirolz.microservice.repository.UserRepository

class UserService(userRepository: UserRepository) {

  def getById(id: UserId): IO[Option[User]] = userRepository.getById(id)
}
