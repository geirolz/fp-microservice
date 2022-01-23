package com.geirolz.microservice.common.config

import cats.Show
import io.circe.Encoder
import pureconfig.ConfigReader

import java.nio.charset.StandardCharsets

case class Secret private (value: Array[Byte]) {

  def stringValue: String = new String(value, StandardCharsets.UTF_8)

  override def toString: String = Secret.placeHolder
}
object Secret {

  val placeHolder = "** MASKED **"

  def apply(value: Array[Byte]): Secret =
    new Secret(value)

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
