package com.geirolz.microservice.infra.config

import pureconfig.ConfigReader

//COMMON
case class SecretString(value: Array[Char]) extends AnyVal {
  override def toString: String = "MASKED"
}
object SecretString {
  implicit val secretConfigReaderInstance: ConfigReader[SecretString] =
    ConfigReader[String].map(n => SecretString(n.toCharArray))
}
