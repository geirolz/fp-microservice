import sbt.ModuleID

final case class Tag private (value: String) extends AnyVal {
  override def toString: String = value
}
object Tag extends TagBuilders with TagInstances {

  def apply(value: String): Tag =
    fromString(value)

  def fromString(value: String): Tag =
    new Tag(value.replace(" ", "-").toLowerCase())
}

sealed trait TagBuilders {

  def fromProcessingPurpose(p: ProcessingPurpose): Tag =
    p match {
      case ProcessingPurpose.OLAP => tag"OLAP"
      case ProcessingPurpose.OLTP => tag"OLTP"
    }

  def fromBoundedContext(ctx: BoundedContext): Tag =
    tag"bounded-context:${ctx.value}"

  def fromDependency(dep: ModuleID): Tag =
    fromVersion(dep.name, dep.revision)

  def fromDependencies(deps: Seq[ModuleID]): Seq[Tag] =
    deps.map(fromDependency)

  def fromScalaVersion(v: String): Tag =
    fromVersion("scala", v)

  def fromVersion(sbj: String, v: String): Tag =
    tag"$sbj:${v.replace(".", "_")}"

  implicit class TagStringContext(stringContext: StringContext) {
    def tag(args: Any*): Tag =
      Tag(stringContext.s(args: _*))
  }
}

sealed trait TagInstances { this: TagBuilders =>
  val microservice: Tag = tag"microservice"
  val readModel: Tag    = tag"read-model"

  object Languages {
    val scala: Tag = tag"scala"
    val java: Tag  = tag"java"
    val shell: Tag = tag"shell"
    val yaml: Tag  = tag"yaml"
  }
}
