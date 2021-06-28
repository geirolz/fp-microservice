package com.geirolz.microservice.infra.config

case class Config(http: HttpConfig, db: DbConfigs) {
  import io.circe.generic.auto._
  import io.circe.syntax._

  override def toString: String = asPrettyString
  def asPrettyString: String = this.asJson.toString()
}
//db
case class DbConfigs(
  main: DbConfig
)
case class DbConfig(
  driver: String,
  url: String,
  user: String,
  pass: SecretString
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
