package com.geirolz.microservice.service

import cats.effect.IO
import com.geirolz.microservice.external.repository.UserRepository
import com.geirolz.microservice.model.User
import com.geirolz.microservice.model.datatype.UserId

class UserService(userRepository: UserRepository) {

  def getById(id: UserId): IO[Option[User]] =
    userRepository.getById(id)
}
