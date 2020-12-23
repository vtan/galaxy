package galaxy.game

import galaxy.clamp
import galaxy.common.{Id, V2}
import galaxy.game.bodies.{BodyType, OrbitalState, SystemNode}
import galaxy.rendering._

import org.lwjgl.glfw.GLFW._
import org.lwjgl.nanovg.NanoVG._

object SystemMap {

  def render()(implicit rc: RenderContext[AppState]): Unit = {
    val gs = rc.appState.gameState
    val selectedStarSystem = gs.starSystems(rc.appState.uiState.selectedStarSystem)
    val orbitalStates = selectedStarSystem.rootNode.orbitalStatesAt(gs.time)

    nvgFontSize(rc.nvg, 16)
    renderNode(selectedStarSystem.rootNode, orbitalStates, parentCenterOnScreen = None)
    handleEvents()
  }

  private def handleEvents()(implicit rc: RenderContext[AppState]): Unit =
    rc.events.foreach {
      case CursorPositionEvent(_, _, xDiff, yDiff) =>
        rc.dispatch(_.mapUiState { ui =>
          if (ui.draggingCamera) {
            val screenDiff = V2(xDiff, yDiff)
            val worldDiff = ui.camera.screenToVector(screenDiff)
            ui.copy(camera = ui.camera.copy(worldPosition = ui.camera.worldPosition - worldDiff))
          } else {
            ui
          }
        })

      case MouseButtonEvent(GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS, _, _, _) =>
        rc.dispatch(_.mapUiState(_.copy(draggingCamera = true)))

      case MouseButtonEvent(GLFW_MOUSE_BUTTON_LEFT, GLFW_RELEASE, _, _, _) =>
        rc.dispatch(_.mapUiState(_.copy(draggingCamera = false)))

      case ScrollEvent(_, y) =>
        rc.dispatch(_.mapUiState { ui =>
          val zoomLevel = Math.cbrt(500 * ui.camera.worldToScreenScale)
          val clamped = clamp(1.5, zoomLevel + 0.5 * y, 70)
          val newScale = clamped * clamped * clamped / 500
          ui.copy(camera = ui.camera.copy(worldToScreenScale = newScale))
        })

      case _ => ()
    }

  private def renderNode(
    systemNode: SystemNode,
    orbitalStates: Map[Id[SystemNode], OrbitalState],
    parentCenterOnScreen: Option[V2[Float]]
  )(implicit rc: RenderContext[AppState]): Unit = {
    val camera = rc.appState.uiState.camera
    val OrbitalState(position, orbitCenter) = orbitalStates(systemNode.id)

    val bodyCenter = camera.pointToScreen(position).map(_.toFloat)
    val distanceSqFromParent = parentCenterOnScreen.map(p => (p - bodyCenter).lengthSq)
    distanceSqFromParent match {
      case Some(distSq) if distSq < 8 * 8 =>
        ()
      case distSqOpt =>
        val small = distSqOpt.exists(_ < 16 * 16)
        nvgBeginPath(rc.nvg)
        if (systemNode.body.bodyType == BodyType.Star) {
          nvgTranslate(rc.nvg, bodyCenter.x, bodyCenter.y)
          nvgScale(rc.nvg, 12, 12)
          Paths.star.draw(rc.nvg)
          nvgResetTransform(rc.nvg)
        } else {
          nvgCircle(rc.nvg, bodyCenter.x, bodyCenter.y, if (small) 4 else 8)
        }
        nvgStrokeColor(rc.nvg, Colors.bodyColors(systemNode.body.bodyType))
        nvgStroke(rc.nvg)

        if (!small) {
          nvgFillColor(rc.nvg, Colors.text)
          nvgText(rc.nvg, bodyCenter.x - 8, bodyCenter.y + 24, systemNode.body.name)

          systemNode.children.foreach(renderNode(_, orbitalStates, Some(bodyCenter)))
        }
    }

    val orbitSize = systemNode.orbit.orbitSize.map(camera.scalarToScreen(_).toFloat)
    if (orbitSize.x > 12 && orbitSize.x < 40_000) {
      val orbitLineCenter = camera.pointToScreen(orbitCenter).map(_.toFloat)
      nvgTranslate(rc.nvg, orbitLineCenter.x, orbitLineCenter.y)
      nvgRotate(rc.nvg, -systemNode.orbit.orbitAngle.toFloat)
      nvgBeginPath(rc.nvg)
      nvgEllipse(rc.nvg, 0, 0, orbitSize.x, orbitSize.y)
      nvgStrokeColor(rc.nvg, Colors.orbit)
      nvgStroke(rc.nvg)
      nvgResetTransform(rc.nvg)
    }
  }
}
