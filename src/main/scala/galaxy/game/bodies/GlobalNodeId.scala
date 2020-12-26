package galaxy.game.bodies

import galaxy.common.Id

final case class GlobalNodeId(
  starSystemId: Id[StarSystem],
  systemNodeId: Id[SystemNode]
)
