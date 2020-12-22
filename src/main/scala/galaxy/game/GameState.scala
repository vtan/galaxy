package galaxy.game

import galaxy.common.Id
import galaxy.game.bodies._
import galaxy.game.dimensions.{Time, TimeDiff}

import scala.util.Random

final case class GameState(
  starSystems: Map[Id[StarSystem], StarSystem],
  time: Time,
  updateSpeed: Option[TimeDiff]
)

object GameState {
  val initial: GameState = GameState(
    starSystems = GalaxyGenerator.generate(new Random()),
    time = Time.epoch,
    updateSpeed = None
  )
}
