package com.geirolz.microservice.infra.route.endpoint.infra.contract

import com.geirolz.microservice.infra.route.endpoint.util.ToContractMapper
import com.geirolz.microservice.model.AppMetricsReport

private[route] case class AppMetricsReportContract(
  usedMemory: Long,
  freeMemory: Long,
  totalMemory: Long,
  maxMemory: Long
)

private[route] object AppMetricsReportContract {

  implicit val appMetricsReportContractMapper: ToContractMapper[AppMetricsReport, AppMetricsReportContract] =
    c =>
      AppMetricsReportContract(
        c.usedMemory,
        c.freeMemory,
        c.totalMemory,
        c.maxMemory
      )
}
