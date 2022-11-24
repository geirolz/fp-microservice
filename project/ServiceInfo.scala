import sbtbuildinfo.BuildInfoPlugin.autoImport.BuildInfoKey

class ServiceInfo private (
  val boundedContext: BoundedContext,
  val processingPurpose: ProcessingPurpose,
  val tags: Set[Tag]
)
object ServiceInfo {

  def of(
    boundedContext: BoundedContext,
    processingPurpose: ProcessingPurpose,
    additionalTags: Set[Tag] = Set.empty
  ): ServiceInfo =
    new ServiceInfo(
      boundedContext,
      processingPurpose,
      Set(
        Tag.fromBoundedContext(boundedContext),
        Tag.fromProcessingPurpose(processingPurpose)
      ) ++ additionalTags
    )

  def deriveBuildInfoKeys(serviceInfo: ServiceInfo): List[BuildInfoKey] =
    Map(
      "boundedContext"    -> serviceInfo.boundedContext,
      "processingPurpose" -> serviceInfo.processingPurpose,
      "tags"              -> serviceInfo.tags.toList.map(_.value)
    ).toList.map(BuildInfoKey(_))
}
