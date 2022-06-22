package com.geirolz.fpmicroservice

import cats.Show
import com.comcast.ip4s.{Hostname, Port}
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Encoder
import org.http4s.Uri
import pureconfig.ConfigReader

import java.nio.charset.StandardCharsets

case class Secret(value: Array[Byte]) extends AnyVal {

  def stringValue: String = new String(value, StandardCharsets.UTF_8)

  override def toString: String = Secret.placeHolder
}
object Secret {

  val placeHolder = "** MASKED **"

  def apply(value: String): Secret =
    Secret(value.getBytes(StandardCharsets.UTF_8))

  implicit val encoderInstanceForSecretString: Encoder[Secret] =
    Encoder.encodeString.contramap(_.toString)

  implicit val configReaderForSecretString: ConfigReader[Secret] =
    ConfigReader.stringConfigReader
      .map(str => Secret(str.getBytes(StandardCharsets.UTF_8)))

  implicit val showInstanceForSecretString: Show[Secret] =
    _ => placeHolder
}

case class AppConfig(http: HttpConfig, db: DbConfigs)
object AppConfig {

  import io.circe.generic.auto.*
  import io.circe.syntax.*
  import io.circe.refined.*
  import eu.timepit.refined.pureconfig.*
  import pureconfig.generic.auto.*
  import pureconfig.generic.semiauto.*
  import pureconfig.module.ip4s.*
  import pureconfig.module.http4s.*

  implicit val configReader: ConfigReader[AppConfig]           = deriveReader[AppConfig]
  implicit val dbConfigReader: ConfigReader[DatabaseConfig]    = deriveReader[DatabaseConfig]
  implicit val serverConfiggReader: ConfigReader[ServerConfig] = deriveReader[ServerConfig]

  // ------------------- CIRCE -------------------
  implicit val hostnameCirceEncoder: Encoder[Hostname] =
    Encoder.encodeString.contramap(_.toString)

  implicit val portCirceEncoder: Encoder[Port] =
    Encoder.encodeInt.contramap(_.value)

  implicit val uriCirceEncoder: Encoder[Uri] =
    Encoder.encodeString.contramap(_.renderString)

  implicit val showInstanceForConfig: Show[AppConfig] = _.asJson.toString()
}

//db
case class DbConfigs(
  main: DatabaseConfig
)

case class DatabaseConfig(
  name: NonEmptyString,
  driver: NonEmptyString,
  url: NonEmptyString,
  username: Option[String],
  password: Option[Secret],
  migrationsTable: NonEmptyString,
  migrationsLocations: List[String]
)

//http
case class HttpConfig(server: ServerConfig)
case class ServerConfig(
  host: Hostname,
  port: Port
)
