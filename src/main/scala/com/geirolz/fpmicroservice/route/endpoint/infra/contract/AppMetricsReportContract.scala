package com.geirolz.fpmicroservice.route.endpoint.infra.contract

import com.geirolz.fpmicroservice.model.AppMetricsReport
import scope.{ModelMapper, Scope}

private[route] case class AppMetricsReportContract(
  usedMemory: Long,
  freeMemory: Long,
  totalMemory: Long,
  maxMemory: Long
)

private[route] object AppMetricsReportContract {

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
