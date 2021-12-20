package com.geirolz.microservice

import cats.effect.{IO, Resource, ResourceIO}
import com.geirolz.microservice.external.repository.UserRepository
import com.geirolz.microservice.service.UserService
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor
import fly4s.core.Fly4s
import fly4s.core.data.{Fly4sConfig, Location}
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.SelfAwareStructuredLogger

import javax.sql.DataSource

case class AppEnv(
  userService: UserService
)
object AppEnv {

  import fly4s.implicits.*
  implicit private val logger: SelfAwareStructuredLogger[IO] =
    Slf4jLogger.getLogger[IO]

  def make(config: Config): Resource[IO, AppEnv] =
    for {

      // -------------------- DB --------------------
      _                <- logger.info("Initializing databases...").to[ResourceIO]
      mainDbTransactor <- createDatabaseTransactor(config.db.main)
      _                <- migrateDatabase(mainDbTransactor.kernel, config.db.main)
      _                <- logger.info("Databases successfully initialized.").to[ResourceIO]

      // ----------------- REPOSITORY ---------------
      userRepository = UserRepository(mainDbTransactor)

    } yield AppEnv(
      userService = UserService(userRepository)
    )

  private def createDatabaseTransactor(
    dbConfig: DatabaseConfig
  ): Resource[IO, HikariTransactor[IO]] =
    for {
      nonBlockingOpsECForDoobie <- ExecutionContexts.fixedThreadPool[IO](32)
      dbPass <- dbConfig.pass
        .map(_.resource[IO](logger.info(_)))
        .getOrElse(Resource.pure[IO, String](""))
      transactor <- HikariTransactor.newHikariTransactor[IO](
        driverClassName = dbConfig.driver,
        url             = dbConfig.url,
        user            = dbConfig.user.getOrElse(""),
        pass            = dbPass,
        nonBlockingOpsECForDoobie
      )
    } yield transactor

  private def migrateDatabase(
    datasource: DataSource,
    dbConfig: DatabaseConfig
  ): Resource[IO, Unit] =
    Fly4s
      .makeFor[IO](
        IO.pure(datasource),
        config = Fly4sConfig(
          table     = dbConfig.migrationsTable,
          locations = Location.of(dbConfig.migrationsLocations*)
        )
      )
      .evalMap(fl4s =>
        for {
          _               <- logger.debug(s"Applying migration for ${dbConfig.name}")
          migrationResult <- fl4s.validateAndMigrate[IO].result
          _ <- logger.info(
            s" Applied ${migrationResult.migrationsExecuted} migrations to ${dbConfig.name} database"
          )
        } yield ()
      )
}
