package galaxy.game.bodies

import galaxy.common.V2

final case class OrbitalState(
  position: V2[Double],
  orbitCenter: V2[Double]
)
