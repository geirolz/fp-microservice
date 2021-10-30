package com.geirolz.microservice.route.endpoint.infra.contract

import com.geirolz.microservice.common.data.{ModelMapper, Scope}
import com.geirolz.microservice.common.data.ModelMapper.ModelMapperId
import com.geirolz.microservice.model.AppMetricsReport

private[route] case class AppMetricsReportContract(
  usedMemory: Long,
  freeMemory: Long,
  totalMemory: Long,
  maxMemory: Long
)

private[route] object AppMetricsReportContract {

  implicit val appMetricsReportContractEndpointMapper
    : ModelMapperId[Scope.Endpoint, AppMetricsReport, AppMetricsReportContract] =
    ModelMapper.lift { c =>
      AppMetricsReportContract(
        c.usedMemory,
        c.freeMemory,
        c.totalMemory,
        c.maxMemory
      )
    }
}
