package com.geirolz.fpmicroservice.http.endpoint.api.infra.contract

import com.geirolz.fpmicroservice.model.AppMetricsReport
import scope.{ModelMapper, Scope}

private[endpoint] case class AppMetricsReportContract(
  usedMemory: Long,
  freeMemory: Long,
  totalMemory: Long,
  maxMemory: Long
)

private[endpoint] object AppMetricsReportContract {

  implicit val scopeEndpointMapper
    : ModelMapper[Scope.Endpoint, AppMetricsReport, AppMetricsReportContract] =
    ModelMapper.scoped[Scope.Endpoint] { c =>
      AppMetricsReportContract(
        c.usedMemory,
        c.freeMemory,
        c.totalMemory,
        c.maxMemory
      )
    }
}
