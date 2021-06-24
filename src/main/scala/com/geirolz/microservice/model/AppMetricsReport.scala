package com.geirolz.microservice.model

import cats.effect.IO

case class AppMetricsReport private (
  usedMemory: Long,
  freeMemory: Long,
  totalMemory: Long,
  maxMemory: Long
)
object AppMetricsReport {
  def fromCurrentRuntime: IO[AppMetricsReport] = IO {

    val runtime = Runtime.getRuntime
    val mb = 1024 * 1024

    AppMetricsReport(
      (runtime.totalMemory - runtime.freeMemory) / mb,
      runtime.freeMemory / mb,
      runtime.totalMemory / mb,
      runtime.maxMemory / mb
    )
  }
}
