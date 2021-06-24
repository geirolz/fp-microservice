package com.geirolz.microservice.repository

import cats.effect.IO
import com.geirolz.microservice.model.value.UserId
import com.geirolz.microservice.repository.entity.UserEntity

class UserRepository() {

  //TODO
  def getById(id: UserId): IO[Option[UserEntity]] = {
    Console.println(id)
    IO.pure(None)
  }

  //noinspection ScalaUnusedSymbol
//  private object SQL {}
}
