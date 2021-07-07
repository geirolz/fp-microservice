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
  )(implicit F: Async[F]): F[ValidatedNel[ValidateOutput, MigrateResult]] = {

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
      config <- F.pure {
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
      validatedFlyway <- initFlyway(config)
      result <- validatedFlyway match {
        case Valid(flyway)        => F.delay(flyway.migrate().valid)
        case invalid @ Invalid(_) => F.pure(invalid)
      }
    } yield result
  }
}
