package com.geirolz.fpmicroservice.http.route

import cats.effect.IO
import com.geirolz.fpmicroservice.http.route.endpoint.DocsEndpoints
import com.geirolz.fpmicroservice.model.AppInfo
import org.http4s.HttpRoutes
import sttp.apispec.openapi.OpenAPI
import sttp.tapir.docs.openapi.{OpenAPIDocsInterpreter, OpenAPIDocsOptions}
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.{SwaggerUI, SwaggerUIOptions}
import sttp.tapir.AnyEndpoint
import sttp.tapir.server.ServerEndpoint

class DocsRoutes private (endpoints: List[AnyEndpoint]) {

  import cats.implicits.*
  import io.circe.syntax.*
  import sttp.apispec.openapi.circe.*
  import sttp.apispec.openapi.circe.yaml.*

  private val interpreter: Http4sServerInterpreter[IO] =
    Http4sServerInterpreter[IO](AppRoutes.defaultServerOptions)

  val openApi: OpenAPI =
    OpenAPIDocsInterpreter(OpenAPIDocsOptions.default)
      .toOpenAPI(
        es      = endpoints,
        title   = AppInfo.value.name,
        version = AppInfo.value.version
      )

  val yamlDocs: String = openApi.toYaml
  val jsonDocs: String = openApi.asJson.deepDropNullValues.toString

  private val yamlDocsRoute: HttpRoutes[IO] = interpreter.toRoutes(
    DocsEndpoints.getYamlDocs.serverLogic(_ => yamlDocs.asRight[Unit].pure[IO])
  )

  private val jsonDocsRoute: HttpRoutes[IO] = interpreter.toRoutes(
    DocsEndpoints.getJsonDocs
      .serverLogic(_ => jsonDocs.asRight[Unit].pure[IO])
  )

  private val swaggerUIRoute: HttpRoutes[IO] = interpreter.toRoutes(
    SwaggerUI[IO](
      yaml = yamlDocs,
      SwaggerUIOptions.default.contextPath(List("docs"))
    )
  )

  val routes: HttpRoutes[IO] =
    yamlDocsRoute <+> jsonDocsRoute <+> swaggerUIRoute
}
object DocsRoutes {
  def fromServerEndpoints[R](serverEndpoints: List[ServerEndpoint[R, IO]]): DocsRoutes =
    new DocsRoutes(serverEndpoints.map(_.endpoint.asInstanceOf[AnyEndpoint]))
}
