package galaxy.game

import galaxy.clamp
import galaxy.common.V2
import galaxy.game.bodies.BodyType
import galaxy.rendering._

import org.lwjgl.glfw.GLFW._
import org.lwjgl.nanovg.NanoVG._

object GalaxyMap {

  def render()(implicit rc: RenderContext[AppState]): Unit = {
    renderStars()
    handleEvents()
  }

  private def renderStars()(implicit rc: RenderContext[AppState]): Unit = {
    val gs = rc.appState.gameState
    val camera = rc.appState.uiState.galaxyCamera

    {
      val selectedStarSystem = gs.starSystems(rc.appState.uiState.selectedStarSystem)
      val center = camera.pointToScreen(selectedStarSystem.position).map(_.toFloat)
      val radius = camera.scalarToScreen(4).toFloat
      nvgBeginPath(rc.nvg)
      nvgCircle(rc.nvg, center.x, center.y, radius)
      nvgStrokeColor(rc.nvg, Colors.bodyColors(BodyType.Star))
      nvgStroke(rc.nvg)
    }

    gs.starSystems.values.foreach { starSystem =>
      val center = camera.pointToScreen(starSystem.position).map(_.toFloat)
      nvgBeginPath(rc.nvg)
      nvgTranslate(rc.nvg, center.x, center.y)
      nvgScale(rc.nvg, 12, 12)
      Paths.star.draw(rc.nvg)
      nvgResetTransform(rc.nvg)
      nvgStrokeColor(rc.nvg, Colors.bodyColors(BodyType.Star))
      nvgStroke(rc.nvg)

      nvgFillColor(rc.nvg, Colors.text)
      nvgTextAlign(rc.nvg, NVG_ALIGN_TOP | NVG_ALIGN_CENTER)
      nvgText(rc.nvg, center.x, center.y + 8, starSystem.name)
      nvgTextAlign(rc.nvg, NVG_ALIGN_LEFT | NVG_ALIGN_BASELINE)
    }
  }

  private def handleEvents()(implicit rc: RenderContext[AppState]): Unit =
    rc.events.foreach {
      case CursorPositionEvent(_, _, xDiff, yDiff) =>
        rc.dispatch(_.mapUiState { ui =>
          if (ui.draggingGalaxyCamera) {
            val screenDiff = V2(xDiff, yDiff)
            val worldDiff = ui.galaxyCamera.screenToVector(screenDiff)
            ui.copy(galaxyCamera = ui.galaxyCamera.copy(worldPosition = ui.galaxyCamera.worldPosition - worldDiff))
          } else {
            ui
          }
        })

      case MouseButtonEvent(GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS, _, x, y) =>
        val camera = rc.appState.uiState.galaxyCamera
        val clickInWorld = camera.screenToPoint(V2(x, y))
        val distanceThreshold = camera.screenToScalar(16)
        val clickedStarSystem = rc.appState.gameState.starSystems.values.find { starSystem =>
          (starSystem.position - clickInWorld).lengthSq <= distanceThreshold * distanceThreshold
        }
        rc.dispatch(_.mapUiState(_.copy(
          draggingGalaxyCamera = true,
          selectedStarSystem = clickedStarSystem.fold(rc.appState.uiState.selectedStarSystem)(_.id)
        )))

      case MouseButtonEvent(GLFW_MOUSE_BUTTON_LEFT, GLFW_RELEASE, _, _, _) =>
        rc.dispatch(_.mapUiState(_.copy(draggingGalaxyCamera = false)))

      case ScrollEvent(_, y) =>
        rc.dispatch(_.mapUiState { ui =>
          val zoomLevel = Math.sqrt(ui.galaxyCamera.worldToScreenScale)
          val clamped = clamp(1, zoomLevel + 0.5 * y, 8)
          val newScale = clamped * clamped
          ui.copy(galaxyCamera = ui.galaxyCamera.copy(worldToScreenScale = newScale))
        })

      case _ => ()
    }
}
