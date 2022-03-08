package com.geirolz.fpmicroservice.common.config

import cats.Show
import io.circe.Encoder
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
