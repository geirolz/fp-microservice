package com.geirolz.microservice

import cats.effect.{IO, Resource}
import com.geirolz.microservice.common.logging.Logging
import com.geirolz.microservice.external.repository.UserRepository
import com.geirolz.microservice.service.UserService
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor
import fly4s.core.Fly4s
import fly4s.core.data.{Fly4sConfig, Location}

import javax.sql.DataSource

case class Env(
  userService: UserService
)
object Env extends Logging.IOLog with Logging.IOResourceLog {

  import fly4s.implicits.*

  def load(config: Config): Resource[IO, Env] =
    for {

      // -------------------- DB --------------------
      _ <- resourceLogger.info("Initializing databases...")
      // main
      mainDbTransactor: HikariTransactor[IO] <- createDbTransactor(config.db.main)
      _ <- applyMigrationToDb(mainDbTransactor.kernel, config.db.main)
      _ <- resourceLogger.info("Databases successfully initialized.")

      // ----------------- REPOSITORY ---------------
      userRepository = UserRepository(mainDbTransactor)

    } yield Env(
      userService = UserService(userRepository)
    )

  private def createDbTransactor(dbConfig: DbConfig): Resource[IO, HikariTransactor[IO]] =
    for {
      nonBlockingOpsECForDoobie <- ExecutionContexts.fixedThreadPool[IO](32)
      dbPass <- dbConfig.pass.map(_.resource[IO](logger.info)).getOrElse(Resource.pure(""))
      transactor <- HikariTransactor.newHikariTransactor[IO](
        driverClassName = dbConfig.driver,
        url             = dbConfig.url,
        user            = dbConfig.user.getOrElse(""),
        pass            = dbPass,
        nonBlockingOpsECForDoobie
      )
    } yield transactor

  private def applyMigrationToDb(
    datasource: DataSource,
    dbConfig: DbConfig
  ): Resource[IO, IO[Unit]] =
    Fly4s
      .makeFor[IO](
        IO.pure(datasource),
        config = Fly4sConfig(
          table     = dbConfig.migrationsTable,
          locations = Location.of(dbConfig.migrationsLocations*)
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
