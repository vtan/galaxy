package galaxy.game.bodies

import galaxy.common.{Id, V2}

final case class JumpPoint(
  destination: Id[StarSystem],
  position: V2[Double]
)
