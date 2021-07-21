package com.geirolz.microservice

import cats.data.Validated.{Invalid, Valid}
import cats.effect.{ContextShift, IO}
import com.geirolz.microservice.external.repository.UserRepository
import com.geirolz.microservice.infra.config.Config
import com.geirolz.microservice.service.UserService
import com.geirolz.microservice.App.logger
import com.geirolz.microservice.common.config.DbConfig
import com.geirolz.microservice.common.db.Database
import doobie.Transactor

case class Env(
  userService: UserService
)
object Env {

  def load(config: Config)(implicit cs: ContextShift[IO]): IO[Env] =
    for {

      //-------------------- DB --------------------
      _                <- logger.debug("Initializing databases...")
      mainDbTransactor <- initDatabase(config.db.main)
      _                <- logger.info("Databases successfully initialized.")

      //----------------- REPOSITORY ---------------
      userRepository = UserRepository(mainDbTransactor)

    } yield Env(
      userService = UserService(userRepository)
    )

  private def initDatabase(dbConfig: DbConfig)(implicit cs: ContextShift[IO]): IO[Transactor[IO]] = {
    for {
      _               <- logger.debug(s"Initializing ${dbConfig.name} database")
      _               <- logger.debug(s"Applying migration for ${dbConfig.name}")
      migrationResult <- Database.migrate[IO](dbConfig)
      _ <- migrationResult match {
        case Valid(result) =>
          logger.info(s" Applied ${result.migrationsExecuted} migrations to ${dbConfig.name} database")
        case Invalid(errors) =>
          IO.raiseError(
            new RuntimeException(
              errors
                .map(error => s"""
                 |Failed validation:
                 |  - version: ${error.version}
                 |  - path: ${error.filepath}
                 |  - description: ${error.description}
                 |  - errorCode: ${error.errorDetails.errorCode}
                 |  - errorMessage: ${error.errorDetails.errorMessage}
                """.stripMargin)
                .toList
                .mkString("\n\n")
            )
          )
      }
    } yield Database.createTransactorUsing[IO](dbConfig)
  }
}
