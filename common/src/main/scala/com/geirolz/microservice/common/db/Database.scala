package com.geirolz.microservice.common.db

import cats.effect.Async
import com.geirolz.microservice.common.config.DbConfig
import doobie.util.transactor.Transactor
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.flywaydb.core.api.output.MigrateResult

object Database {

  import cats.implicits._

  def createTransactorUsing[F[_]: Async](config: DbConfig): Transactor[F] =
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
  )(implicit F: Async[F]): F[MigrateResult] =
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
    }.flatMap(migrate[F])

  def migrate[F[_]](
    flywayConfig: FluentConfiguration
  )(implicit F: Async[F]): F[MigrateResult] = Async[F].delay {
    flywayConfig.load().migrate()
  }
}
