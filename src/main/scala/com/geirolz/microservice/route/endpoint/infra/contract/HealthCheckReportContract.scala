package com.geirolz.microservice.route.endpoint.infra.contract

import cats.effect.IO
import com.geirolz.microservice.model.HealthCheckReport
import com.geirolz.microservice.route.util.ToDomainMapper

private[route] case class HealthCheckReportContract(version: String)

object HealthCheckReportContract {

  implicit val healthCheckReportContractMapper: ToDomainMapper[HealthCheckReportContract, HealthCheckReport] =
    c => IO.pure(HealthCheckReport(c.version))
}
