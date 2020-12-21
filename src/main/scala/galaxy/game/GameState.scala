package galaxy.game

import galaxy.common.Id
import galaxy.game.bodies.{Body, OrbitNode, OrbitalState, SolarSystem}
import galaxy.game.dimensions.{Time, TimeDiff}

final case class GameState(
  bodies: Map[Id[Body], Body],
  rootOrbitNode: OrbitNode,
  time: Time,
  updateSpeed: Option[TimeDiff]
) {
  lazy val orbitalStates: Map[Id[Body], OrbitalState] =
    rootOrbitNode.orbitalStatesAt(time)
}

object GameState {
  val initial: GameState = GameState(
    bodies = SolarSystem.bodies,
    rootOrbitNode = SolarSystem.rootOrbitNode,
    time = Time.epoch,
    updateSpeed = None
  )
}
