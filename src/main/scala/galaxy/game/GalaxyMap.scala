package galaxy.game

import galaxy.clamp
import galaxy.common.V2
import galaxy.game.bodies.BodyType
import galaxy.rendering._

import com.softwaremill.quicklens._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.nanovg.NanoVG._

object GalaxyMap {

  def render()(implicit rc: RenderContext[AppState]): Unit = {
    nvgTextAlign(rc.nvg, NVG_ALIGN_TOP | NVG_ALIGN_CENTER)
    renderStars()
    nvgTextAlign(rc.nvg, NVG_ALIGN_LEFT | NVG_ALIGN_BASELINE)

    handleEvents()
  }

  private def renderStars()(implicit rc: RenderContext[AppState]): Unit = {
    val gs = rc.appState.gameState
    val camera = rc.appState.uiState.galaxyCamera
    val small = camera.worldToScreenScale < 7
    val boundingRect = camera.worldBoundingRect(rc.screenSize.map(_.toDouble))

    if (!small) {
      val selectedStarSystem = gs.starSystems(rc.appState.uiState.selectedStarSystem)
      if (boundingRect.contains(selectedStarSystem.position)) {
        val center = camera.pointToScreen(selectedStarSystem.position).map(_.toFloat)
        nvgBeginPath(rc.nvg)
        nvgCircle(rc.nvg, center.x, center.y, 32)
        nvgStrokeColor(rc.nvg, Colors.ship)
        nvgStroke(rc.nvg)
      }
    }

    nvgFontSize(rc.nvg, 16)
    gs.starSystems.values.foreach { starSystem =>
      if (boundingRect.contains(starSystem.position)) {
        val center = camera.pointToScreen(starSystem.position).map(_.toFloat)
        nvgBeginPath(rc.nvg)
        if (small) {
          nvgRect(rc.nvg, center.x, center.y, 1, 1)
        } else {
          nvgTranslate(rc.nvg, center.x, center.y)
          nvgScale(rc.nvg, 12, 12)
          Paths.star.draw(rc.nvg)
          nvgResetTransform(rc.nvg)
        }
        nvgStrokeColor(rc.nvg, Colors.bodyColors(BodyType.Star))
        nvgStroke(rc.nvg)

        if (!small) {
          nvgFillColor(rc.nvg, if (starSystem.visited) Colors.text else Colors.faintLabel)
          nvgText(rc.nvg, center.x, center.y + 8, starSystem.name)
        }

        starSystem.jumpPoints.foreach { jumpPoint =>
          val destination = gs.starSystems(jumpPoint.destination)
          if (destination.visited) {
            val other = camera.pointToScreen(destination.position).map(_.toFloat)
            nvgBeginPath(rc.nvg)
            nvgMoveTo(rc.nvg, center.x, center.y)
            nvgLineTo(rc.nvg, other.x, other.y)
            nvgStrokeColor(rc.nvg, Colors.jumpLine)
            nvgStroke(rc.nvg)
          }
        }
      }
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
        clickedStarSystem match {
          case Some(starSystem) =>
            rc.dispatch(_.modify(_.gameState.starSystems.index(starSystem.id).visited).setTo(true))
          case None => ()
        }

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
