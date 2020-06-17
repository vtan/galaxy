package galaxy

final case class RenderContext(
  nvg: Long,
  screenSize: V2[Double],
  gameState: GameState,
  uiState: UiState,
  var gameStateUpdates: List[GameState => GameState],
  var uiStateUpdates: List[UiState => UiState]
) {

  def updateGameState(update: GameState => GameState): Unit =
    gameStateUpdates = update :: gameStateUpdates

  def updateUiState(update: UiState => UiState): Unit =
    uiStateUpdates = update :: uiStateUpdates

  def applyUpdates: RenderContext =
    copy(
      gameState = Function.chain(gameStateUpdates.reverse)(gameState),
      uiState = Function.chain(uiStateUpdates.reverse)(uiState),
      gameStateUpdates = List.empty,
      uiStateUpdates = List.empty
    )
}