package com.geirolz.microservice.common.config

case class DbConfig(
  name: String,
  driver: String,
  url: String,
  user: Option[String],
  pass: Option[SecretString],
  migrationsTable: String,
  migrationsLocations: List[String]
)
