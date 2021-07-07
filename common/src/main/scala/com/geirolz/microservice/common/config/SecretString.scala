package com.geirolz.microservice.common.config

import cats.Show
import io.circe.Encoder
import pureconfig.ConfigReader

class SecretString private (value: Array[Char]) {
  def stringValue: String = value.mkString
  override def toString: String = SecretString.placeHolder
}
object SecretString {

  val placeHolder = "** MASKED **"

  def apply(value: Array[Char]): SecretString = new SecretString(value)

  def apply(value: String): SecretString = new SecretString(value.toCharArray)

  implicit val encoderInstanceForSecretString: Encoder[SecretString] =
    Encoder.encodeString.contramap(_.toString)

  implicit val configReaderForSecretString: ConfigReader[SecretString] =
    ConfigReader[String].map(n => SecretString(n.toCharArray))

  implicit val showInstanceForSecretString: Show[SecretString] =
    _ => placeHolder
}
