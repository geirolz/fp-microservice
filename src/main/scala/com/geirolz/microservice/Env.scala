package com.geirolz.microservice

import cats.effect.IO
import com.geirolz.microservice.external.repository.UserRepository
import com.geirolz.microservice.infra.config.Config
import com.geirolz.microservice.service.UserService
import com.geirolz.microservice.App.logger
import com.geirolz.microservice.common.config.DbConfig
import com.geirolz.microservice.common.db.Database
import doobie.Transactor
import fly4s.core.Fly4s
import fly4s.core.data.{Fly4sConfig, Location}

case class Env(
  userService: UserService
)
object Env {

  import fly4s.implicits._

  def load(config: Config): IO[Env] =
    for {

      //-------------------- DB --------------------
      _ <- logger.debug("Initializing databases...")
//      mainDbTransactor <- initDatabase(config.db.main)
      _ <- logger.info("Databases successfully initialized.")

      //----------------- REPOSITORY ---------------
      userRepository = UserRepository(Database.createTransactorUsing[IO](config.db.main))

    } yield Env(
      userService = UserService(userRepository)
    )

  private def initDatabase(dbConfig: DbConfig): IO[Transactor[IO]] = {
    for {
      _ <- logger.debug(s"Initializing ${dbConfig.name} database")
      _ <- logger.debug(s"Applying migration for ${dbConfig.name}")
      migrationResult <- Fly4s(
        Fly4sConfig(
          url = dbConfig.url,
          user = dbConfig.user,
          password = dbConfig.pass.map(_.stringValue.toCharArray),
          table = dbConfig.migrationsTable,
          locations = Location.ofAll(dbConfig.migrationsLocations: _*)
        )
      ).validateAndMigrate[IO].result
      _ <- logger.info(s" Applied ${migrationResult.migrationsExecuted} migrations to ${dbConfig.name} database")
    } yield Database.createTransactorUsing[IO](dbConfig)
  }
}
