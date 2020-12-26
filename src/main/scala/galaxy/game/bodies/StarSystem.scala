package galaxy.game.bodies

import galaxy.common.{Id, V2}
import galaxy.game.ships.Ship

final case class StarSystem(
  id: Id[StarSystem],
  name: String,
  position: V2[Double],
  rootNode: SystemNode,
  jumpPoints: Vector[JumpPoint],
  visited: Boolean,
  ships: Map[Id[Ship], Ship]
)
