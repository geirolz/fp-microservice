package com.geirolz.microservice.repository

import cats.effect.IO
import com.geirolz.microservice.model.User
import com.geirolz.microservice.model.value.UserId

class UserRepository() {

  //TODO
  def getById(id: UserId): IO[Option[User]] = {
    Console.println(id)
    IO.pure(None)
  }

  //noinspection ScalaUnusedSymbol
//  private object SQL {}
}
