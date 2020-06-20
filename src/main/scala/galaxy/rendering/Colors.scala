package galaxy.rendering

import org.lwjgl.nanovg.NVGColor

object Colors {
  val body = createColor(1.0f, 1.0f, 0, 1.0f)
  val orbit = createColor(1.0f, 0.75f, 0, 0.5f)
  val text = createColor(1, 1, 1, 1)

  val button = createColor(0.4f, 0.4f, 0.4f, 1)
  val buttonActive = createColor(0.8f, 0.8f, 0.4f, 1)
  val buttonBorder = createColor(0.6f, 0.6f, 0.6f, 1)

  private def createColor(r: Float, g: Float, b: Float, a: Float): NVGColor = {
    val c = NVGColor.create()
    c.r(r)
    c.g(g)
    c.b(b)
    c.a(a)
  }
}
