package galaxy.game.dimensions

final case class Time(asSeconds: Long) extends AnyVal {

  def +(diff: TimeDiff): Time =
    copy(asSeconds = asSeconds + diff.asSeconds)

  override def toString: String = {
    val sec = asSeconds % 60
    val min = asSeconds / 60 % 60
    val hour = asSeconds / 3600 % 24
    val day = 1 + asSeconds / (24 * 3600) % 30
    val month = 1 + asSeconds / (30 * 24 * 3600) % 12
    val year = 2100 + asSeconds / (12 * 20 * 24 * 3600)
    f"$year%d-$month%02d-$day%02d $hour%02d:$min%02d:$sec%02d"
  }
}

object Time {
  val epoch: Time = Time(0)
}

final case class TimeDiff(asSeconds: Long) extends AnyVal
