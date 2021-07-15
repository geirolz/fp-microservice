package com.geirolz.microservice.infra.route.endpoint.infra.contract

import com.geirolz.microservice.common.data.{Endpoint, ModelMapper}
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
    : ModelMapperId[Endpoint, AppMetricsReport, AppMetricsReportContract] =
    ModelMapper.lift { c =>
      AppMetricsReportContract(
        c.usedMemory,
        c.freeMemory,
        c.totalMemory,
        c.maxMemory
      )
    }
}
