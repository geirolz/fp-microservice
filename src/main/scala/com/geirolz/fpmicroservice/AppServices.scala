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

case class AppServices(
  userService: UserService
)
object AppServices {

  private val logger: SelfAwareStructuredLogger[IO] =
    Slf4jLogger.getLogger[IO]

  def make(config: AppConfig): Resource[IO, AppServices] =
    for {

      // -------------------- DB --------------------
      _                <- logger.info("Initializing databases...").to[ResourceIO]
      mainDbTransactor <- createDatabaseTransactor(config.db.main)
      _                <- migrateDatabase(mainDbTransactor.kernel, config.db.main)
      _                <- logger.info("Databases successfully initialized.").to[ResourceIO]

      // ----------------- REPOSITORY ---------------
      userRepository = UserRepository(mainDbTransactor)

    } yield AppServices(
      userService = UserService(userRepository)
    )

  private def createDatabaseTransactor(
    dbConfig: DatabaseConfig
  ): Resource[IO, HikariTransactor[IO]] =
    for {
      nonBlockingOpsECForDoobie <- ExecutionContexts.fixedThreadPool[IO](32)
      transactor <- HikariTransactor.newHikariTransactor[IO](
        driverClassName = dbConfig.driver.value,
        url             = dbConfig.url.value,
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
        config = Fly4sConfig.default
          .withTable(dbConfig.migrationsTable.value)
          .withLocations(Location.of(dbConfig.migrationsLocations*))
      )
      .evalMap(fl4s =>
        for {
          _ <- logger.debug(s"Applying migration for ${dbConfig.name}")
          result <- fl4s.migrate.onError { ex =>
            logger.error(ex)(
              s"Unable to apply database ${dbConfig.name} migrations."
            )
          }
          _ <- logger.info(
            s"Applied ${result.migrationsExecuted} " +
              s"migrations to ${dbConfig.name} database"
          )
        } yield ()
      )
}
