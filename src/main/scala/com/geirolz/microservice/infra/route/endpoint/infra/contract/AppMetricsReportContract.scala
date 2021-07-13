package com.geirolz.microservice.infra.route.endpoint.infra.contract

import com.geirolz.microservice.common.data.{Endpoint, ModelScopeMapper}
import com.geirolz.microservice.common.data.ModelScopeMapper.ModelScopeMapperId
import com.geirolz.microservice.model.AppMetricsReport

private[route] case class AppMetricsReportContract(
  usedMemory: Long,
  freeMemory: Long,
  totalMemory: Long,
  maxMemory: Long
)

private[route] object AppMetricsReportContract {

  implicit val appMetricsReportContractEndpointMapper
    : ModelScopeMapperId[Endpoint, AppMetricsReport, AppMetricsReportContract] =
    ModelScopeMapper.id(c =>
      AppMetricsReportContract(
        c.usedMemory,
        c.freeMemory,
        c.totalMemory,
        c.maxMemory
      )
    )
}
