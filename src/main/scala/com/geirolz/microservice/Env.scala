package com.geirolz.microservice

import cats.effect.IO
import com.geirolz.microservice.infra.config.Config
import com.geirolz.microservice.repository.UserRepository
import com.geirolz.microservice.service.UserService

case class Env(
  userService: UserService
)
object Env {

  //noinspection ScalaUnusedSymbol
  def build(config: Config): IO[Env] = IO {

    Console.println(config)

    //repos
    val userRepository = new UserRepository()

    //env
    Env(
      userService = new UserService(userRepository)
    )
  }
}
