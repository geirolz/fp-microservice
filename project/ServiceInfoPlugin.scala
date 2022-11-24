import sbt.{settingKey, KeyRanks, SettingKey}

object ServiceInfoPlugin {

  object Keys {
    val serviceInfo: SettingKey[ServiceInfo] =
      settingKey[ServiceInfo]("Service info")
        .withRank(KeyRanks.Invisible)

    val serviceProcessingPurpose: SettingKey[ProcessingPurpose] =
      settingKey[ProcessingPurpose]("Service processing purpose")
        .withRank(KeyRanks.Invisible)
  }
}
