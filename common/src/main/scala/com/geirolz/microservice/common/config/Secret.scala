package com.geirolz.microservice.common.config

import cats.Show
import cats.effect.{Async, Resource}
import io.circe.Encoder
import pureconfig.ConfigReader

class Secret private (key: String, value: Array[Char]) {

  import cats.implicits.*

  def resource[F[_]: Async](
    logAction: String => F[Unit]
  ): Resource[F, String] = {
    var s = value.mkString
    Resource.make(
      logAction(s"Using secret '$key'").as(s)
    )(_ =>
      Async[F].delay {
        s = null
        System.gc()
      }
    )
  }

  override def toString: String = Secret.placeHolder
}
object Secret {

  val placeHolder = "** MASKED **"

  def apply(key: String, value: Array[Char]): Secret = new Secret(key, value)

  def apply(key: String, value: String): Secret = Secret(key, value.toCharArray)

  implicit val encoderInstanceForSecretString: Encoder[Secret] =
    Encoder.encodeString.contramap(_.toString)

  implicit val configReaderForSecretString: ConfigReader[Secret] =
    ConfigReader.fromCursor(c => {
      c.asString.map(s => Secret(c.path, s.toCharArray))
    })

  implicit val showInstanceForSecretString: Show[Secret] =
    _ => placeHolder
}
