import sbt.addCompilerPlugin
import com.geirolz.sbt.serviceinfo._
import sbtwelcome._

lazy val appName               = "fp-microservice"
lazy val appDescription        = "Basic template for microservices."
lazy val appOrg                = "com.geirolz"
lazy val appPackage            = s"$appOrg.$appName".replace(" ", "").replace("-", "")
lazy val appScalaVersion       = "2.13.13"
lazy val appDockerExposedPorts = Seq(8080)

//------------------------------------------------------------------------------
lazy val global = (project in file("."))
  .enablePlugins(BuildInfoPlugin, ServiceInfoPlugin, JavaAppPackaging, DockerPlugin)
  .settings(
    addCommandAlias("validate", ";scalafmtSbtCheck;scalafmtCheckAll;compile;test;"),
    addCommandAlias("dockerPublishValidLocal", ";validate;docker:publishLocal")
  )
  .settings(commonSettings: _*)
  .settings(logoSettings: _*)
  .settings(
    name := appName,
    description := appDescription,
    organization := appOrg,
    // build info
    // info
    serviceBoundedContext := BoundedContext("template"),
    serviceProcessingPurpose := ProcessingPurpose.OLTP,
    buildInfoKeys ++= List[BuildInfoKey](
      name,
      description,
      version,
      scalaVersion,
      sbtVersion,
      buildInfoBuildNumber
    ),
    buildInfoOptions ++= List(
      BuildInfoOption.BuildTime,
      BuildInfoOption.PackagePrivate,
      BuildInfoOption.ConstantValue
    ),
    buildInfoPackage := appPackage,
    Compile / mainClass := Some(s"$appPackage.AppMain")
  )
  .settings(dockerSettings: _*)

//------------------------------------------------------------------------------
lazy val dockerSettings: Seq[Setting[_]] = Seq(
  dockerUpdateLatest := true,
  dockerExposedPorts := appDockerExposedPorts
)

lazy val logoSettings: Seq[Setting[_]] = {

  def info(label: String, value: String): String =
    s"$label: ${scala.Console.YELLOW}$value${scala.Console.RESET}"

  Seq(
    logo :=
      // https://patorjk.com/software/taag/#p=display&f=ANSI%20Regular&t=FP%20SERVICE
      s"""
         |███████ ██████      ███████ ███████ ██████  ██    ██ ██  ██████ ███████
         |██      ██   ██     ██      ██      ██   ██ ██    ██ ██ ██      ██
         |█████   ██████      ███████ █████   ██████  ██    ██ ██ ██      █████
         |██      ██               ██ ██      ██   ██  ██  ██  ██ ██      ██
         |██      ██          ███████ ███████ ██   ██   ████   ██  ██████ ███████
         |
         |${info("Context", serviceBoundedContext.value.value)}
         |${info("Purpose", serviceProcessingPurpose.value.toString())}
         |${description.value}
         |
         |-------------------------------
         |${info("Version", version.value)}
         |${info("Scala", scalaVersion.value)}
         |${info("SBT", sbtVersion.value)}
         |""".stripMargin,
    usefulTasks := List(
      UsefulTask("run", "Start application"),
      UsefulTask("test", "Run unit tests"),
      UsefulTask("it:test", "Run integration unit tests"),
      UsefulTask("scalafmtCheckAll;test;it:test;", "Run unit and integration unit tests"),
      UsefulTask("~compile", "Compile with file-watch enabled"),
      UsefulTask("scalafmtAll", "Run scalafmt on the entire project")
    )
  )
}
lazy val commonSettings: Seq[Setting[_]] = Seq(
  // scala
  scalaVersion := appScalaVersion,
  scalacOptions ++= scalacSettings,
  // dependencies
  resolvers ++= ProjectResolvers.all,
  libraryDependencies ++= ProjectDependencies.common,
  // fmt
  scalafmtOnCompile := true,
  // plugins
  addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
  addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
)

def scalacSettings: Seq[String] =
  Seq(
    //    "-Xlog-implicits",
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-encoding",
    "utf-8", // Specify character encoding used by source files.
    "-explaintypes", // Explain type errors in more detail.
    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
    "-language:experimental.macros", // Allow macro definition (besides implementation and application)
    "-language:higherKinds", // Allow higher-kinded types
    "-language:implicitConversions", // Allow definition of implicit functions called views
    "-unchecked", // Enable additional warnings where generated code depends on assumptions.
    "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
    "-Xfatal-warnings", // Fail the compilation if there are any warnings.
    "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
    "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
    "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
    "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
    "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
    "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
    "-Xlint:option-implicit", // Option.apply used implicit view.
    "-Xlint:package-object-classes", // Class or object defined in package object.
    "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
    "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
    "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
    "-Ywarn-dead-code", // Warn when dead code is identified.
    "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
    "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
    "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
    "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
//    "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
//    "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
//    "-Ywarn-unused:locals", // Warn if a local definition is unused.
//    "-Ywarn-unused:explicits", // Warn if a explicit value parameter is unused.
//    "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
//    "-Ywarn-unused:privates", // Warn if a private member is unused.
    "-Ywarn-macros:after", // Tells the compiler to make the unused checks after macro expansion
    "-Xsource:3",
    "-P:kind-projector:underscore-placeholders"
  )
