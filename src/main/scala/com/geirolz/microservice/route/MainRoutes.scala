package com.geirolz.microservice.route

import cats.effect.IO
import com.geirolz.microservice.model.{AppInfo, AppMetricsReport}
import com.geirolz.microservice.route.endpoint.infra.InfraEndpointsApi
import com.geirolz.microservice.route.endpoint.{DocsEndpointsApi, EndpointsApi}
import com.geirolz.microservice.route.endpoint.user.UserEndpointApi
import org.http4s.HttpRoutes
import sttp.tapir.docs.openapi.{OpenAPIDocsInterpreter, OpenAPIDocsOptions}
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.Endpoint
import com.geirolz.microservice.common.data.Scope

class MainRoutes private () {

  import cats.implicits.*
  import com.geirolz.microservice.common.data.ModelMapper.*
  import com.geirolz.microservice.route.endpoint.infra.contract.AppInfoContract.*
  import com.geirolz.microservice.route.endpoint.infra.contract.AppMetricsReportContract.*

  import io.circe.syntax.*
  import sttp.tapir.openapi.circe.*
  import sttp.tapir.openapi.circe.yaml.*

  private val http4sInterpreter: Http4sServerInterpreter[IO] =
    Http4sServerInterpreter[IO]()

  private val appInfoRoute =
    http4sInterpreter.toRoutes(InfraEndpointsApi.getAppInfo) { _ =>
      IO.pure(AppInfo.value.toScopeId[Scope.Endpoint].asRight[Unit])
    }

  private val appMetricsRoute =
    http4sInterpreter.toRoutes(InfraEndpointsApi.getAppMetrics) { _ =>
      AppMetricsReport.fromCurrentRuntime.map(_.toScopeId[Scope.Endpoint].asRight[Unit])
    }

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

    http4sInterpreter.toRoutes(DocsEndpointsApi.getYamlDocs) { _ =>
      IO.pure(Right(yamlDocs))
    } <+>
    http4sInterpreter.toRoutes(DocsEndpointsApi.getJsonDocs) { _ =>
      IO.pure(Right(jsonDocs))
    } <+>
    http4sInterpreter.toRoutes(
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
