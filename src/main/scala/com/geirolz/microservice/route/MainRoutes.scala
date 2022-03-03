package com.geirolz.microservice.route

import cats.effect.IO
import com.geirolz.microservice.model.{AppInfo, AppMetricsReport}
import com.geirolz.microservice.route.endpoint.{DocsEndpointsApi, EndpointsApi}
import com.geirolz.microservice.route.endpoint.infra.InfraEndpointsApi
import com.geirolz.microservice.route.endpoint.infra.contract.{
  AppInfoContract,
  AppMetricsReportContract
}
import org.http4s.HttpRoutes
import scope.{InScope, Scope}
import sttp.tapir.docs.openapi.{OpenAPIDocsInterpreter, OpenAPIDocsOptions}
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.SwaggerUI

class MainRoutes private () extends InScope[Scope.Endpoint] {

  import cats.implicits.*
  import io.circe.syntax.*
  import scope.syntax.*
  import sttp.tapir.openapi.circe.*
  import sttp.tapir.openapi.circe.yaml.*

  private val interpreter: Http4sServerInterpreter[IO] =
    Http4sServerInterpreter[IO](ServerConfiguration.options)

  private val appInfoRoute =
    interpreter.toRoutes(
      InfraEndpointsApi.getAppInfo
        .serverLogic(_ => AppInfo.value.scoped.as[AppInfoContract].asRight[Unit].pure[IO])
    )

  private val appMetricsRoute =
    interpreter.toRoutes(
      InfraEndpointsApi.getAppMetrics
        .serverLogic(_ =>
          AppMetricsReport.fromCurrentRuntime.map(
            _.scoped.as[AppMetricsReportContract].asRight[Unit]
          )
        )
    )

  private val docsRoute: HttpRoutes[IO] = {

    val openApi: OpenAPI =
      OpenAPIDocsInterpreter(OpenAPIDocsOptions.default)
        .toOpenAPI(
          EndpointsApi.all,
          title   = AppInfo.value.name,
          version = AppInfo.value.version
        )

    val yamlDocs: String = openApi.toYaml
    val jsonDocs: String = openApi.asJson.deepDropNullValues.toString

    interpreter.toRoutes(
      DocsEndpointsApi.getYamlDocs.serverLogic(_ => yamlDocs.asRight[Unit].pure[IO])
    )
    <+>
    interpreter.toRoutes(
      DocsEndpointsApi.getJsonDocs.serverLogic(_ => jsonDocs.asRight[Unit].pure[IO])
    )
    interpreter.toRoutes(
      SwaggerUI[IO](
        yaml   = yamlDocs,
        prefix = List("docs")
      )
    )
  }

  val routes: HttpRoutes[IO] =
    appInfoRoute <+>
      appMetricsRoute <+>
      docsRoute
}
object MainRoutes {
  def make: MainRoutes = new MainRoutes()
}
