package com.geirolz.microservice.common.db

import cats.effect.Async
import com.geirolz.microservice.common.config.DbConfig
import doobie.util.transactor.Transactor

object Database {

  def createTransactorUsing[F[_]: Async](config: DbConfig): Transactor[F] =
    (config.user, config.pass) match {
      case (Some(user), Some(pass)) =>
        Transactor.fromDriverManager[F](
          driver = config.driver,
          url = config.url,
          user = user,
          pass = pass.stringValue
        )
      case _ =>
        Transactor.fromDriverManager[F](
          driver = config.driver,
          url = config.url
        )
    }
}
