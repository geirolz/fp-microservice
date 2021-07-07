package com.geirolz.microservice.infra.config

import cats.Show
import com.geirolz.microservice.common.config.DbConfig

case class Config(http: HttpConfig, db: DbConfigs)
object Config {

  import io.circe.generic.auto._
  import io.circe.syntax._

  implicit val showInstanceForConfig: Show[Config] = _.asJson.toString()
}

//db
case class DbConfigs(
  main: DbConfig
)

//http
case class HttpConfig(server: ServerConfig)
case class ServerConfig(
  host: String,
  port: Int,
  logging: ServerLoggingConfig
)
case class ServerLoggingConfig(
  request: HttpLoggingConfig,
  response: HttpLoggingConfig
)
case class HttpLoggingConfig(logHeaders: Boolean, logBody: Boolean)
