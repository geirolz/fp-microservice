package com.geirolz.microservice

import cats.Show
import com.geirolz.microservice.common.config.Secret

case class Config(http: HttpConfig, db: DbConfigs)
object Config {

  import io.circe.generic.auto.*
  import io.circe.syntax.*

  implicit val showInstanceForConfig: Show[Config] = _.asJson.toString()
}

//db
case class DbConfigs(
  main: DbConfig
)

case class DbConfig(
  name: String,
  driver: String,
  url: String,
  user: Option[String],
  pass: Option[Secret],
  migrationsTable: String,
  migrationsLocations: List[String]
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
