package galaxy

import galaxy.bodies.{OrbitNode, OrbitalState, SolarSystem}
import org.lwjgl.glfw.GLFW._
import org.lwjgl.nanovg.NanoVG._
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.opengl.GL11C._

class Renderer(
  screenSize: V2[Double],
  nvg: Long
) {

  private var camera = Camera(
    worldPosition = V2.zero,
    screenCenter = 0.5 *: screenSize,
    worldToScreenScale = 200
  )
  private var gameState = GameState(
    bodies = SolarSystem.bodies,
    rootOrbitNode = SolarSystem.rootOrbitNode,
    time = 0
  )

  private val bodyColor = createColor(1.0f, 1.0f, 0, 1.0f)
  private val orbitColor = createColor(1.0f, 0.75f, 0, 0.5f)
  private val textColor = createColor(1, 1, 1, 1)

  private val font = nvgCreateFont(nvg, "Roboto", "assets/roboto/Roboto-Regular.ttf")
  nvgFontFaceId(nvg, font)

  private var lastCursorPosition: CursorPositionEvent = CursorPositionEvent(0, 0)
  private var draggingMouse: Boolean = false

  def render(events: List[GlfwEvent]): Unit = {
    gameState = StepLogic.stepTime(diff = 60 * 60)(gameState)
    events.foreach {
      case e @ CursorPositionEvent(x, y) =>
        if (draggingMouse) {
          val startPosition = V2(lastCursorPosition.x, lastCursorPosition.y)
          val endPosition = V2(x, y)
          val screenDiff = startPosition - endPosition
          val worldDiff = camera.screenToVector(screenDiff)
          camera = camera.copy(worldPosition = camera.worldPosition + worldDiff)
        }
        lastCursorPosition = e

      case MouseButtonEvent(GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS, _) =>
        draggingMouse = true

      case MouseButtonEvent(GLFW_MOUSE_BUTTON_LEFT, GLFW_RELEASE, _) =>
        draggingMouse = false

      case ScrollEvent(_, y) =>
        val zoomLevel = Math.cbrt(camera.worldToScreenScale)
        val clamped = clamp(1.5, zoomLevel + 0.5 * y, 70)
        val newScale = clamped * clamped * clamped
        camera = camera.copy(worldToScreenScale = newScale)

      case _ => ()
    }

    glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

    nvgBeginFrame(nvg, screenSize.x.toFloat, screenSize.y.toFloat, 1)
    nvgFontSize(nvg, 20)
    renderOrbitNode(gameState.rootOrbitNode, parentCenterOnScreen = None)
    nvgEndFrame(nvg)
  }

  private def renderOrbitNode(orbitNode: OrbitNode, parentCenterOnScreen: Option[V2[Float]]): Unit = {
    val OrbitalState(position, orbitCenter) = gameState.orbitalStates(orbitNode.bodyId)

    val bodyCenter = camera.pointToScreen(position).map(_.toFloat)
    val distanceSqFromParent = parentCenterOnScreen.map(p => (p - bodyCenter).lengthSq)
    distanceSqFromParent match {
      case Some(distSq) if distSq < 8 * 8 =>
        ()
      case Some(distSq) if distSq < 16 * 16 =>
        nvgBeginPath(nvg)
        nvgCircle(nvg, bodyCenter.x, bodyCenter.y, 4)
        nvgStrokeColor(nvg, bodyColor)
        nvgStroke(nvg)
      case _ =>
        nvgBeginPath(nvg)
        nvgCircle(nvg, bodyCenter.x, bodyCenter.y, 8)
        nvgStrokeColor(nvg, bodyColor)
        nvgStroke(nvg)
        nvgFillColor(nvg, textColor)
        nvgText(nvg, bodyCenter.x - 8, bodyCenter.y + 24, gameState.bodies(orbitNode.bodyId).name)

        orbitNode.children.foreach(renderOrbitNode(_, Some(bodyCenter)))
    }

    val orbitLineRadius = camera.scalarToScreen(orbitNode.orbitRadius).toFloat
    if (orbitLineRadius > 12 && orbitLineRadius < 40_000) {
      val orbitLineCenter = camera.pointToScreen(orbitCenter).map(_.toFloat)
      nvgBeginPath(nvg)
      nvgCircle(nvg, orbitLineCenter.x, orbitLineCenter.y, orbitLineRadius)
      nvgStrokeColor(nvg, orbitColor)
      nvgStroke(nvg)
    }
  }

  private def createColor(r: Float, g: Float, b: Float, a: Float): NVGColor = {
    val c = NVGColor.create()
    c.r(r)
    c.g(g)
    c.b(b)
    c.a(a)
  }
}
