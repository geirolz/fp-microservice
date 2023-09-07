package com.geirolz.fpmicroservice.http.endpoint.docs

import cats.effect.IO
import com.geirolz.fpmicroservice.AppInfo
import sttp.apispec.openapi.OpenAPI
import sttp.tapir.*
import sttp.tapir.docs.openapi.{OpenAPIDocsInterpreter, OpenAPIDocsOptions}
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.{SwaggerUI, SwaggerUIOptions}

private[http] class DocsEndpoints private (appInfo: AppInfo, endpoints: List[AnyEndpoint]) {

  import cats.implicits.*
  import io.circe.syntax.*
  import sttp.apispec.openapi.circe.*
  import sttp.apispec.openapi.circe.yaml.*

  private val openApi: OpenAPI =
    OpenAPIDocsInterpreter(OpenAPIDocsOptions.default)
      .toOpenAPI(
        es      = endpoints,
        title   = appInfo.name.value,
        version = appInfo.version.value
      )

  private val yamlDocs: String = openApi.toYaml
  private val jsonDocs: String = openApi.asJson.deepDropNullValues.toString

  private val yamlDocsRoute: ServerEndpoint[Any, IO] =
    DocsEndpoints.Def.getYamlDocs.serverLogic(_ => yamlDocs.asRight[Unit].pure[IO])

  private val jsonDocsRoute: ServerEndpoint[Any, IO] =
    DocsEndpoints.Def.getJsonDocs
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
private[http] object DocsEndpoints {

  def fromServerEndpoints[R](
    appInfo: AppInfo,
    serverEndpoints: List[ServerEndpoint[R, IO]]
  ): DocsEndpoints =
    new DocsEndpoints(appInfo, serverEndpoints.map(_.endpoint.asInstanceOf[AnyEndpoint]))

  object Def {

    val getJsonDocs: PublicEndpoint[Unit, Unit, String, Any] =
      endpoint
        .in("docs" / "swagger.json")
        .out(stringBody)

    val getYamlDocs: PublicEndpoint[Unit, Unit, String, Any] =
      endpoint
        .in("docs" / "swagger.yaml")
        .out(stringBody)

  }
}
