package com.geirolz.fpmicroservice.route

import cats.effect.IO
import com.geirolz.fpmicroservice.model.AppInfo
import com.geirolz.fpmicroservice.route.endpoint.{DocsEndpointsApi, EndpointsApi}
import com.geirolz.fpmicroservice.AppRoutes
import org.http4s.HttpRoutes
import sttp.apispec.openapi.OpenAPI
import sttp.tapir.docs.openapi.{OpenAPIDocsInterpreter, OpenAPIDocsOptions}
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.{SwaggerUI, SwaggerUIOptions}

class DocsRoutes private {

  import cats.implicits.*
  import io.circe.syntax.*
  import sttp.apispec.openapi.circe.*
  import sttp.apispec.openapi.circe.yaml.*

  private val interpreter: Http4sServerInterpreter[IO] =
    Http4sServerInterpreter[IO](AppRoutes.defaultServerOptions)

  val openApi: OpenAPI =
    OpenAPIDocsInterpreter(OpenAPIDocsOptions.default)
      .toOpenAPI(
        EndpointsApi.all,
        title   = AppInfo.value.name,
        version = AppInfo.value.version
      )

  val yamlDocs: String = openApi.toYaml
  val jsonDocs: String = openApi.asJson.deepDropNullValues.toString

  private val yamlDocsRoute: HttpRoutes[IO] = interpreter.toRoutes(
    DocsEndpointsApi.getYamlDocs.serverLogic(_ => yamlDocs.asRight[Unit].pure[IO])
  )

  private val jsonDocsRoute: HttpRoutes[IO] = interpreter.toRoutes(
    DocsEndpointsApi.getJsonDocs
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
  def make: DocsRoutes = new DocsRoutes
}
