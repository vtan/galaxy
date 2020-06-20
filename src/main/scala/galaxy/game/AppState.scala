package galaxy.game

final case class AppState(
  gameState: GameState,
  uiState: UiState
) {

  def mapGameState(f: GameState => GameState): AppState =
    copy(gameState = f(gameState))

  def mapUiState(f: UiState => UiState): AppState =
    copy(uiState = f(uiState))
}
