package com.geirolz.microservice

import cats.effect.{IO, Resource}
import com.geirolz.microservice.common.config.DbConfig
import com.geirolz.microservice.common.logging.Logging
import com.geirolz.microservice.external.repository.UserRepository
import com.geirolz.microservice.infra.config.Config
import com.geirolz.microservice.service.UserService
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor
import fly4s.core.Fly4s
import fly4s.core.data.{Fly4sConfig, Location}

case class Env(
  userService: UserService
)
object Env extends Logging.IOLog with Logging.IOResourceLog {

  import fly4s.implicits._

  def load(config: Config): Resource[IO, Env] =
    for {

      //-------------------- DB --------------------
      _ <- resourceLogger.info("Initializing databases...")
      //main
      mainDbTransactor <- createDbTransactor(config.db.main)
      _                <- applyMigrationToDb(config.db.main)
      _                <- resourceLogger.info("Databases successfully initialized.")

      //----------------- REPOSITORY ---------------
      userRepository = UserRepository(mainDbTransactor)

    } yield Env(
      userService = UserService(userRepository)
    )

  private def createDbTransactor(dbConfig: DbConfig): Resource[IO, HikariTransactor[IO]] =
    for {
      nonBlockingOpsECForDoobie <- ExecutionContexts.fixedThreadPool[IO](32)
      transactor <- HikariTransactor.newHikariTransactor[IO](
        driverClassName = dbConfig.driver,
        url             = dbConfig.url,
        user            = dbConfig.user.getOrElse(""),
        pass            = dbConfig.pass.fold("")(_.stringValue),
        nonBlockingOpsECForDoobie
      )
    } yield transactor

  private def applyMigrationToDb(dbConfig: DbConfig): Resource[IO, IO[Unit]] =
    Fly4s
      .make[IO](
        url      = dbConfig.url,
        user     = dbConfig.user,
        password = dbConfig.pass.map(_.stringValue.toCharArray),
        config = Fly4sConfig(
          table     = dbConfig.migrationsTable,
          locations = Location.of(dbConfig.migrationsLocations: _*)
        )
      )
      .map(fl4s =>
        for {
          _               <- logger.debug(s"Applying migration for ${dbConfig.name}")
          migrationResult <- fl4s.validateAndMigrate[IO].result
          _ <- logger.info(
            s" Applied ${migrationResult.migrationsExecuted} migrations to ${dbConfig.name} database"
          )
        } yield ()
      )
}
