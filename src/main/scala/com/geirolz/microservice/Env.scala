package com.geirolz.microservice

import cats.data.Validated.{Invalid, Valid}
import cats.effect.{ContextShift, IO}
import com.geirolz.microservice.external.repository.UserRepository
import com.geirolz.microservice.infra.config.{Config, DbConfig}
import com.geirolz.microservice.infra.db.Db
import com.geirolz.microservice.service.UserService
import com.geirolz.microservice.App.logger
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
      userRepository = new UserRepository(mainDbTransactor)

    } yield Env(
      userService = new UserService(userRepository)
    )

  private def initDatabase(dbConfig: DbConfig)(implicit cs: ContextShift[IO]): IO[Transactor[IO]] = {
    for {
      _               <- logger.debug(s"Initializing ${dbConfig.name} database")
      _               <- logger.debug(s"Applying migration for ${dbConfig.name}")
      migrationResult <- Db.applyMigration(dbConfig)
      _ <- migrationResult match {
        case Valid(migrationsCount) =>
          logger.info(s" Applied $migrationsCount migrations to ${dbConfig.name} database")
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
    } yield Db.createTransactorFor(dbConfig)
  }
}
