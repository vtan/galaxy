package galaxy.game

import galaxy.common.{Camera, V2}

final case class UiState(
  camera: Camera,
  draggingCamera: Boolean
)

object UiState {
  def initial(screenSize: V2[Double]): UiState = UiState(
    camera = Camera(
      worldPosition = V2.zero,
      screenCenter = 0.5 *: screenSize,
      worldToScreenScale = 200
    ),
    draggingCamera = false
  )
}
