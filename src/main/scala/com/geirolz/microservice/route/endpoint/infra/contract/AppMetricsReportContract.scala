package com.geirolz.microservice.route.endpoint.infra.contract

import com.geirolz.microservice.model.AppMetricsReport
import com.geirolz.microservice.route.endpoint.util.ToContractMapper

private[route] case class AppMetricsReportContract(
  usedMemory: Long,
  freeMemory: Long,
  totalMemory: Long,
  maxMemory: Long
)

private[route] object AppMetricsReportContract {

  implicit val appeMetricsReportContractMapper: ToContractMapper[AppMetricsReport, AppMetricsReportContract] =
    c =>
      AppMetricsReportContract(
        c.usedMemory,
        c.freeMemory,
        c.totalMemory,
        c.maxMemory
      )
}
