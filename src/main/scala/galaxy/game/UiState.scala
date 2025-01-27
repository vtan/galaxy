package galaxy.game

import galaxy.common.{Camera, Id, V2}
import galaxy.game.bodies.{GlobalNodeId, StarSystem}
import galaxy.game.dimensions.TimeDiff

final case class UiState(
  updateSpeed: Option[TimeDiff],
  selectedStarSystem: Id[StarSystem],
  selectedSystemNode: GlobalNodeId,
  systemMapOpen: Boolean,
  camera: Camera,
  draggingCamera: Boolean,
  galaxyCamera: Camera,
  draggingGalaxyCamera: Boolean
)

object UiState {
  def initial(screenSize: V2[Double]): UiState = UiState(
    updateSpeed = None,
    selectedStarSystem = Id(0),
    selectedSystemNode = GlobalNodeId(Id(0), Id(3)),
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
