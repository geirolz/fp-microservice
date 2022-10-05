import sbt.{settingKey, KeyRanks, ModuleID, SettingKey}

object ProjectInfo {
  object Keys {
    val infoTags: SettingKey[Seq[String]] =
      settingKey[Seq[String]]("Projects info tags").withRank(KeyRanks.Invisible)
    val boundedContext: SettingKey[String] =
      settingKey[String]("Projects bounded context").withRank(KeyRanks.Invisible)
    val processingPurpose: SettingKey[ProcessingPurpose] =
      settingKey[ProcessingPurpose]("Projects processing purpose").withRank(KeyRanks.Invisible)
  }

  sealed trait ProcessingPurpose
  object ProcessingPurpose {
    // Online analytical processing
    case object OLAP extends ProcessingPurpose
    // Online transactional processing
    case object OLTP extends ProcessingPurpose
  }

  object Tags {
    val scala: String        = "scala"
    val microservice: String = "microservice"

    def fromProcessingPurpose(p: ProcessingPurpose): String =
      p match {
        case ProcessingPurpose.OLAP => "OLAP"
        case ProcessingPurpose.OLTP => "OLTP"
      }

    def fromScalaVersion(v: String): String =
      normalizeVersion("scala", v)

    def fromDependencies(deps: Seq[ModuleID]): Seq[String] =
      deps.map(m => normalizeVersion(m.name, m.revision))

    def normalizeVersion(sbj: String, v: String): String =
      s"$sbj:${v.replaceAllLiterally(".", "_")}"
  }
}
