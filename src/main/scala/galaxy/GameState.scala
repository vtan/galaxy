package galaxy

import galaxy.bodies.{Body, OrbitNode, OrbitalState}

final case class GameState(
  bodies: Map[Id[Body], Body],
  rootOrbitNode: OrbitNode,
  time: Long
) {
  lazy val orbitalStates: Map[Id[Body], OrbitalState] =
    rootOrbitNode.orbitalStatesAt(time)
}
