import sbt.{settingKey, KeyRanks, SettingKey}

object ServiceInfoPlugin {

  object Keys {
    val serviceInfo: SettingKey[ServiceInfo] =
      settingKey[ServiceInfo]("Service info")
        .withRank(KeyRanks.Invisible)
  }
}
