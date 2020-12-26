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
) {
  lazy val nodesById: Map[Id[SystemNode], SystemNode] =
    Map.from(rootNode.treeIterator.map(n => n.id -> n))
}
