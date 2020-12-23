package galaxy.game.bodies

import galaxy.common.{Id, V2}

final case class StarSystem(
  id: Id[StarSystem],
  name: String,
  position: V2[Double],
  rootNode: SystemNode,
  jumpPoints: Vector[JumpPoint]
)
