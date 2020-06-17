package galaxy

object StepLogic {

  def stepTime(diff: Long)(gs: GameState): GameState =
    gs.copy(time = gs.time + diff)
}
