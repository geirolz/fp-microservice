package com.geirolz.fpmicroservice

import cats.effect.{IO, Resource, ResourceIO}
import com.geirolz.app.toolkit.App
import com.geirolz.app.toolkit.novalues.NoResources
import com.geirolz.fpmicroservice.external.repository.UserRepository
import com.geirolz.fpmicroservice.service.UserService
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor
import fly4s.core.Fly4s
import fly4s.core.data.{Fly4sConfig, Location}
import org.typelevel.log4cats.SelfAwareStructuredLogger

import javax.sql.DataSource

case class AppDependentServices(
  userService: UserService
)
object AppDependentServices {

  def fromAppResources(
    appResources: App.Resources[AppInfo, SelfAwareStructuredLogger[IO], AppConfig, NoResources]
  ): Resource[IO, AppDependentServices] = {
    val logger = appResources.logger
    val config = appResources.config
    for {
      // -------------------- DB --------------------
      _                <- logger.info("Initializing databases...").to[ResourceIO]
      mainDbTransactor <- databaseTransactorResource(config.db.main)
      _                <- logger.info("Databases successfully initialized.").to[ResourceIO]

      // ----------------- REPOSITORY ---------------
      userRepository = UserRepository(mainDbTransactor)

    } yield AppDependentServices(
      userService = UserService(userRepository)
    )
  }

  private def databaseTransactorResource(
    dbConfig: DatabaseConfig
  ): ResourceIO[HikariTransactor[IO]] =
    for {
      nonBlockingOpsECForDoobie <- ExecutionContexts.fixedThreadPool[IO](32)
      transactor <- HikariTransactor.newHikariTransactor[IO](
        driverClassName = dbConfig.driver.value,
        url             = dbConfig.url.value,
        user            = dbConfig.username.getOrElse(""),
        pass            = dbConfig.password.map(_.unsafeUse).getOrElse(""),
        connectEC       = nonBlockingOpsECForDoobie
      )
    } yield transactor
}
