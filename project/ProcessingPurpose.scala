
sealed trait ProcessingPurpose {
  override def toString: String = this match {
    case ProcessingPurpose.OLAP => "OLAP"
    case ProcessingPurpose.OLTP => "OLTP"
  }
}
object ProcessingPurpose {
  // Online analytical processing
  final case object OLAP extends ProcessingPurpose
  // Online transactional processing
  final case object OLTP extends ProcessingPurpose
}
