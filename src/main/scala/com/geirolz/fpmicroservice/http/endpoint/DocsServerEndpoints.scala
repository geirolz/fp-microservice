package com.geirolz.fpmicroservice.http.endpoint

import cats.effect.IO
import com.geirolz.fpmicroservice.http.endpoint.api.DocsEndpoints
import com.geirolz.fpmicroservice.{App, AppInfo}
import sttp.apispec.openapi.OpenAPI
import sttp.tapir.docs.openapi.{OpenAPIDocsInterpreter, OpenAPIDocsOptions}
import sttp.tapir.swagger.{SwaggerUI, SwaggerUIOptions}
import sttp.tapir.AnyEndpoint
import sttp.tapir.server.ServerEndpoint

private[http] class DocsServerEndpoints private (appInfo: AppInfo, endpoints: List[AnyEndpoint]) {

  import cats.implicits.*
  import io.circe.syntax.*
  import sttp.apispec.openapi.circe.*
  import sttp.apispec.openapi.circe.yaml.*

  val openApi: OpenAPI =
    OpenAPIDocsInterpreter(OpenAPIDocsOptions.default)
      .toOpenAPI(
        es      = endpoints,
        title   = appInfo.name.value,
        version = appInfo.version.value
      )

  private val yamlDocs: String = openApi.toYaml
  private val jsonDocs: String = openApi.asJson.deepDropNullValues.toString

  private val yamlDocsRoute: ServerEndpoint[Any, IO] =
    DocsEndpoints.getYamlDocs.serverLogic(_ => yamlDocs.asRight[Unit].pure[IO])

  private val jsonDocsRoute: ServerEndpoint[Any, IO] =
    DocsEndpoints.getJsonDocs
      .serverLogic(_ => jsonDocs.asRight[Unit].pure[IO])

  private val swaggerUIRoute: List[ServerEndpoint[Any, IO]] =
    SwaggerUI[IO](
      yaml = yamlDocs,
      SwaggerUIOptions.default.contextPath(List("docs"))
    )

  val serverEndpoints: List[ServerEndpoint[Any, IO]] =
    List(
      yamlDocsRoute,
      jsonDocsRoute
    ) ++ swaggerUIRoute
}
private[http] object DocsServerEndpoints {
  def fromServerEndpoints[R](
    appInfo: AppInfo,
    serverEndpoints: List[ServerEndpoint[R, IO]]
  ): DocsServerEndpoints =
    new DocsServerEndpoints(appInfo, serverEndpoints.map(_.endpoint.asInstanceOf[AnyEndpoint]))
}
