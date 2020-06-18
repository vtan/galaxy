package galaxy

import galaxy.bodies.{OrbitalState, OrbitNode}

import org.lwjgl.glfw.GLFW._
import org.lwjgl.nanovg.NanoVG._

object SystemMap {

  def render()(implicit rc: RenderContext): Unit = {
    renderOrbitNode(rc.gameState.rootOrbitNode, parentCenterOnScreen = None)
    rc.updateGameState(StepLogic.stepTime(diff = 60 * 60))
    handleEvents()
  }

  private def handleEvents()(implicit rc: RenderContext): Unit =
    rc.events.foreach {
      case CursorPositionEvent(_, _, xDiff, yDiff) =>
        rc.updateUiState { ui =>
          if (ui.draggingCamera) {
            val screenDiff = V2(xDiff, yDiff)
            val worldDiff = ui.camera.screenToVector(screenDiff)
            ui.copy(camera = ui.camera.copy(worldPosition = ui.camera.worldPosition - worldDiff))
          } else {
            ui
          }
        }

      case MouseButtonEvent(GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS, _) =>
        rc.updateUiState(_.copy(draggingCamera = true))

      case MouseButtonEvent(GLFW_MOUSE_BUTTON_LEFT, GLFW_RELEASE, _) =>
        rc.updateUiState(_.copy(draggingCamera = false))

      case ScrollEvent(_, y) =>
        rc.updateUiState { ui =>
          val zoomLevel = Math.cbrt(ui.camera.worldToScreenScale)
          val clamped = clamp(1.5, zoomLevel + 0.5 * y, 70)
          val newScale = clamped * clamped * clamped
          ui.copy(camera = ui.camera.copy(worldToScreenScale = newScale))
        }

      case _ => ()
    }

  private def renderOrbitNode(orbitNode: OrbitNode, parentCenterOnScreen: Option[V2[Float]])(implicit rc: RenderContext): Unit = {
    val gs = rc.gameState
    val camera = rc.uiState.camera
    val OrbitalState(position, orbitCenter) = gs.orbitalStates(orbitNode.bodyId)

    val bodyCenter = camera.pointToScreen(position).map(_.toFloat)
    val distanceSqFromParent = parentCenterOnScreen.map(p => (p - bodyCenter).lengthSq)
    distanceSqFromParent match {
      case Some(distSq) if distSq < 8 * 8 =>
        ()
      case Some(distSq) if distSq < 16 * 16 =>
        nvgBeginPath(rc.nvg)
        nvgCircle(rc.nvg, bodyCenter.x, bodyCenter.y, 4)
        nvgStrokeColor(rc.nvg, Colors.body)
        nvgStroke(rc.nvg)
      case _ =>
        nvgBeginPath(rc.nvg)
        nvgCircle(rc.nvg, bodyCenter.x, bodyCenter.y, 8)
        nvgStrokeColor(rc.nvg, Colors.body)
        nvgStroke(rc.nvg)
        nvgFillColor(rc.nvg, Colors.text)
        nvgText(rc.nvg, bodyCenter.x - 8, bodyCenter.y + 24, gs.bodies(orbitNode.bodyId).name)

        orbitNode.children.foreach(renderOrbitNode(_, Some(bodyCenter)))
    }

    val orbitLineRadius = camera.scalarToScreen(orbitNode.orbitRadius).toFloat
    if (orbitLineRadius > 12 && orbitLineRadius < 40_000) {
      val orbitLineCenter = camera.pointToScreen(orbitCenter).map(_.toFloat)
      nvgBeginPath(rc.nvg)
      nvgCircle(rc.nvg, orbitLineCenter.x, orbitLineCenter.y, orbitLineRadius)
      nvgStrokeColor(rc.nvg, Colors.orbit)
      nvgStroke(rc.nvg)
    }
  }
}
