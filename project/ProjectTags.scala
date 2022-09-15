import sbt.{settingKey, SettingKey}

object ProjectTags {
  object Keys {
    val projectInfoTags: SettingKey[List[String]] = settingKey[List[String]]("Projects info tags")
  }

  val scala: String = "scala"
  val microservice: String = "microservice"
}
