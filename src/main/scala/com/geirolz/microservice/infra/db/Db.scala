package com.geirolz.microservice.infra.db

import cats.data.{NonEmptyList, ValidatedNel}
import cats.effect.{ContextShift, IO}
import com.geirolz.microservice.infra.config.DbConfig
import doobie.util.transactor.Transactor
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location
import org.flywaydb.core.api.output.ValidateOutput

import scala.jdk.CollectionConverters.ListHasAsScala

object Db {

  import cats.implicits._

  def createTransactorFor(config: DbConfig)(implicit cs: ContextShift[IO]): Transactor[IO] =
    (config.user, config.pass) match {
      case (Some(user), Some(pass)) =>
        Transactor.fromDriverManager[IO](
          driver = config.driver,
          url = config.url,
          user = user,
          pass = pass.stringValue
        )
      case _ =>
        Transactor.fromDriverManager[IO](
          driver = config.driver,
          url = config.url
        )
    }

  def applyMigration(
    dbConfig: DbConfig,
    group: Boolean = false,
    outOfOrder: Boolean = false,
    baselineOnMigrate: Boolean = false,
    ignorePendingMigrations: Boolean = false
  ): IO[ValidatedNel[ValidateOutput, Int]] = {

    def toValidatedNel(flyway: Flyway): ValidatedNel[ValidateOutput, Flyway] = {
      val validateResult = flyway.validateWithResult()
      if (!validateResult.validationSuccessful) {
        NonEmptyList
          .fromListUnsafe(
            validateResult.invalidMigrations.asScala.toList
          )
          .invalid
      } else {
        flyway.valid
      }
    }

    for {
      config <- IO.pure {
        Flyway.configure
          .dataSource(
            dbConfig.url,
            dbConfig.user.orNull,
            dbConfig.pass.map(_.stringValue).orNull
          )
          .group(group)
          .outOfOrder(outOfOrder)
          .baselineOnMigrate(baselineOnMigrate)
          .ignorePendingMigrations(ignorePendingMigrations)
          .table(dbConfig.migrationsTable)
          .locations(
            dbConfig.migrationsLocations.map(new Location(_)): _*
          )
      }
      flyway <- IO(toValidatedNel(config.load()))
      result <- IO(flyway.map(_.migrate()))
    } yield result.map(_.migrationsExecuted)
  }
}
