package galaxy.game

object StepLogic {

  // TODO assumes 60 fps
  def stepTime(gs: GameState): GameState = {
    gs.updateSpeed match {
      case Some(dt) => gs.copy(time = gs.time + dt)
      case None => gs
    }
  }
}
