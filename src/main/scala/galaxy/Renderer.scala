package galaxy

import galaxy.bodies.{OrbitalState, SolarSystem}

import org.lwjgl.nanovg.NanoVG._
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.opengl.GL11C._

class Renderer(
  screenSize: V2[Double],
  nvg: Long
) {

  private val camera = Camera(
    worldPosition = V2.zero,
    screenCenter = 0.5 *: screenSize,
    worldToScreenScale = 200
  )

  private val bodyColor = createColor(1.0f, 1.0f, 0, 1.0f)
  private val orbitColor = createColor(1.0f, 0.75f, 0, 0.5f)

  private val rootOrbitNode = SolarSystem.rootOrbitNode

  private val orbitalStates = rootOrbitNode.orbitalStatesAt(time = 0)

  def render(): Unit = {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

    nvgBeginFrame(nvg, screenSize.x.toFloat, screenSize.y.toFloat, 1)

    rootOrbitNode.toDepthFirstSeq.foreach { orbitNode =>
      val OrbitalState(position, orbitCenter) = orbitalStates(orbitNode.bodyId)

      val bodyCenter = camera.pointToScreen(position).map(_.toFloat)
      val orbitLineCenter = camera.pointToScreen(orbitCenter).map(_.toFloat)
      val orbitLineRadius = camera.scalarToScreen(orbitNode.orbitRadius).toFloat

      nvgBeginPath(nvg)
      nvgCircle(nvg, bodyCenter.x, bodyCenter.y, 8)
      nvgStrokeColor(nvg, bodyColor)
      nvgStroke(nvg)

      nvgBeginPath(nvg)
      nvgCircle(nvg, orbitLineCenter.x, orbitLineCenter.y, orbitLineRadius)
      nvgStrokeColor(nvg, orbitColor)
      nvgStroke(nvg)
    }

    nvgEndFrame(nvg)
  }

  private def createColor(r: Float, g: Float, b: Float, a: Float): NVGColor = {
    val c = NVGColor.create()
    c.r(r)
    c.g(g)
    c.b(b)
    c.a(a)
  }
}
