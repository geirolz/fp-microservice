package com.geirolz.microservice

import cats.effect.{ContextShift, IO}
import com.geirolz.microservice.infra.config.Config
import com.geirolz.microservice.repository.UserRepository
import com.geirolz.microservice.service.UserService
import doobie.util.transactor.Transactor

case class Env(
  userService: UserService
)
object Env {

  //noinspection ScalaUnusedSymbol
  def build(config: Config)(implicit cs: ContextShift[IO]): Env = {

    //db
    val mainDbTransactor = Transactor.fromDriverManager[IO](
      driver = config.db.main.driver,
      url = config.db.main.url,
      user = config.db.main.user,
      pass = config.db.main.pass.value.mkString
    )

    //repos
    val userRepository = new UserRepository(mainDbTransactor)

    //env
    Env(
      userService = new UserService(userRepository)
    )
  }
}
