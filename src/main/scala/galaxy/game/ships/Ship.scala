package galaxy.game.ships

import galaxy.common.{Id, V2}
import galaxy.game.bodies.SystemNode

final case class Ship(
  id: Id[Ship],
  name: String,
  position: ShipPosition
)

sealed trait ShipPosition

object ShipPosition {
  final case class AtNode(nodeId: Id[SystemNode]) extends ShipPosition
  final case class InSpace(position: V2[Double]) extends ShipPosition
}
