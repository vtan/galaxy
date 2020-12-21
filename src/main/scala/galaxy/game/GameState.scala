package galaxy.game

import galaxy.common.Id
import galaxy.game.bodies.{OrbitalState, SolarSystem, SystemNode}
import galaxy.game.dimensions.{Time, TimeDiff}

final case class GameState(
  rootSystemNode: SystemNode,
  time: Time,
  updateSpeed: Option[TimeDiff]
) {
  lazy val orbitalStates: Map[Id[SystemNode], OrbitalState] =
    rootSystemNode.orbitalStatesAt(time)
}

object GameState {
  val initial: GameState = GameState(
    rootSystemNode = SolarSystem.rootSystemNode,
    time = Time.epoch,
    updateSpeed = None
  )
}
