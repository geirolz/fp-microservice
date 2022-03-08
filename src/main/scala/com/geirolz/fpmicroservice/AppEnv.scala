package com.geirolz.fpmicroservice

import cats.effect.{IO, Resource, ResourceIO}
import com.geirolz.fpmicroservice.external.repository.UserRepository
import com.geirolz.fpmicroservice.service.UserService
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
      transactor <- HikariTransactor.newHikariTransactor[IO](
        driverClassName = dbConfig.driver,
        url             = dbConfig.url,
        user            = dbConfig.username.getOrElse(""),
        pass            = dbConfig.password.map(_.stringValue).getOrElse(""),
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
        logger.debug(s"Applying migration for ${dbConfig.name}") >>
          fl4s.migrate.attempt
            .flatMap {
              case Left(ex) =>
                logger.error(ex)(
                  s"Unable to apply database ${dbConfig.name} migrations."
                )
              case Right(result) =>
                logger.info(
                  s"Applied ${result.migrationsExecuted} " +
                    s"migrations to ${dbConfig.name} database"
                )
            }
      )
}
