package galaxy.game

import galaxy.common.Id
import galaxy.game.bodies._
import galaxy.game.dimensions.Time

import scala.util.Random

final case class GameState(
  starSystems: Map[Id[StarSystem], StarSystem],
  time: Time
)

object GameState {
  val initial: GameState = GameState(
    starSystems = GalaxyGenerator.generate(new Random()),
    time = Time.epoch
  )
}
