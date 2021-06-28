package com.geirolz.microservice.infra.config

import io.circe.Encoder
import pureconfig.ConfigReader

//COMMON
case class SecretString(private val value: Array[Char]) {
  def stringValue: String = value.mkString
  override def toString: String = "** MASKED **"
}
object SecretString {

  implicit val secretStringEncoder: Encoder[SecretString] = Encoder.encodeString.contramap(_.toString)

  implicit val secretStringConfigReader: ConfigReader[SecretString] =
    ConfigReader[String].map(n => SecretString(n.toCharArray))
}
