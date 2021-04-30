package com.geirolz.microservice.infra.config

case class Config(http: HttpConfig)

//htt4ps
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
