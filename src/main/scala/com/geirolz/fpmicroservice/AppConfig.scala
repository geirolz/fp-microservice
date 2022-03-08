package com.geirolz.fpmicroservice

import cats.Show
import com.comcast.ip4s.{Hostname, Port}
import com.geirolz.fpmicroservice.common.config.Secret
import io.circe.Encoder
import org.http4s.Uri
import pureconfig.ConfigReader

case class Config(http: HttpConfig, db: DbConfigs)
object Config {

  import io.circe.generic.auto.*
  import io.circe.syntax.*
  import pureconfig.generic.auto.*
  import pureconfig.generic.semiauto.*
  import pureconfig.module.ip4s.*
  import pureconfig.module.http4s.*

  implicit val configReader: ConfigReader[Config]              = deriveReader[Config]
  implicit val dbConfigReader: ConfigReader[DatabaseConfig]    = deriveReader[DatabaseConfig]
  implicit val serverConfiggReader: ConfigReader[ServerConfig] = deriveReader[ServerConfig]

  // ------------------- CIRCE -------------------
  implicit val hostnameCirceEncoder: Encoder[Hostname] =
    Encoder.encodeString.contramap(_.toString)

  implicit val portCirceEncoder: Encoder[Port] =
    Encoder.encodeInt.contramap(_.value)

  implicit val uriCirceEncoder: Encoder[Uri] =
    Encoder.encodeString.contramap(_.renderString)

  implicit val showInstanceForConfig: Show[Config] = _.asJson.toString()
}

//db
case class DbConfigs(
  main: DatabaseConfig
)

case class DatabaseConfig(
  name: String,
  driver: String,
  url: String,
  username: Option[String],
  password: Option[Secret],
  migrationsTable: String,
  migrationsLocations: List[String]
)

//http
case class HttpConfig(server: ServerConfig)
case class ServerConfig(
  host: Hostname,
  port: Port,
  logging: ServerLoggingConfig
)
case class ServerLoggingConfig(
  request: HttpLoggingConfig,
  response: HttpLoggingConfig
)
case class HttpLoggingConfig(logHeaders: Boolean, logBody: Boolean)
