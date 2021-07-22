package com.geirolz.microservice.common.db

import cats.data.{NonEmptyList, ValidatedNel}
import cats.data.Validated.{Invalid, Valid}
import cats.effect.{Async, ContextShift}
import com.geirolz.microservice.common.config.DbConfig
import doobie.util.transactor.Transactor
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.flywaydb.core.api.output.{MigrateResult, ValidateOutput}

import scala.jdk.CollectionConverters.ListHasAsScala

object Database {

  import cats.implicits._

  type MigrationResult = ValidatedNel[ValidateOutput, MigrateResult]

  def createTransactorUsing[F[_]: Async: ContextShift](config: DbConfig): Transactor[F] =
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

  def migrate[F[_]](
    dbConfig: DbConfig,
    group: Boolean = false,
    outOfOrder: Boolean = false,
    baselineOnMigrate: Boolean = false,
    ignorePendingMigrations: Boolean = false
  )(implicit F: Async[F]): F[MigrationResult] =
    F.delay {
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
    }.flatMap(migrate)

  def migrate[F[_]](
    config: FluentConfiguration
  )(implicit F: Async[F]): F[MigrationResult] = {

    def initFlyway(flywayConfig: FluentConfiguration): F[ValidatedNel[ValidateOutput, Flyway]] =
      for {
        flyway         <- F.delay(flywayConfig.load())
        validateResult <- F.delay(flyway.validateWithResult())
        res <- validateResult.validationSuccessful match {
          case true => F.pure(flyway.valid)
          case false =>
            NonEmptyList
              .fromList(validateResult.invalidMigrations.asScala.toList)
              .map(_.invalid)
              .liftTo[F](new RuntimeException(""))
        }
      } yield res

    for {
      validatedFlyway <- initFlyway(config)
      result <- validatedFlyway match {
        case Valid(flyway)        => F.delay(flyway.migrate().valid)
        case invalid @ Invalid(_) => F.pure(invalid)
      }
    } yield result
  }

  def evalMigrationResult[F[_]](
    result: MigrationResult
  )(implicit F: Async[F]): F[MigrateResult] = {
    result match {
      case Valid(result) => F.pure(result)
      case Invalid(errors) =>
        F.raiseError(
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
  }
}
