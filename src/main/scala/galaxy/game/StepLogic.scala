package galaxy.game

import galaxy.game.dimensions.TimeDiff

object StepLogic {

  def stepTime(step: TimeDiff)(gs: GameState): GameState =
    gs.copy(time = gs.time + step)
}
