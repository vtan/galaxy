package galaxy.game

import galaxy.common.Id
import galaxy.game.bodies.{Body, OrbitalState, OrbitNode, SolarSystem}

final case class GameState(
  bodies: Map[Id[Body], Body],
  rootOrbitNode: OrbitNode,
  time: Long
) {
  lazy val orbitalStates: Map[Id[Body], OrbitalState] =
    rootOrbitNode.orbitalStatesAt(time)
}

object GameState {
  val initial: GameState = GameState(
    bodies = SolarSystem.bodies,
    rootOrbitNode = SolarSystem.rootOrbitNode,
    time = 0
  )
}
