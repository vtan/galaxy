package galaxy.game

import galaxy.common.{Camera, Id, V2}
import galaxy.game.bodies.StarSystem

final case class UiState(
  selectedStarSystem: Id[StarSystem],
  systemMapOpen: Boolean,
  camera: Camera,
  draggingCamera: Boolean,
  galaxyCamera: Camera,
  draggingGalaxyCamera: Boolean
)

object UiState {
  def initial(screenSize: V2[Double]): UiState = UiState(
    selectedStarSystem = Id(0),
    systemMapOpen = false,
    camera = Camera(
      worldPosition = V2.zero,
      screenCenter = 0.5 *: screenSize,
      worldToScreenScale = 0.4
    ),
    draggingCamera = false,
    galaxyCamera = Camera(
      worldPosition = V2.zero,
      screenCenter = 0.5 *: screenSize,
      worldToScreenScale = 25
    ),
    draggingGalaxyCamera = false
  )
}
