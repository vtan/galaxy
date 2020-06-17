package galaxy

import org.lwjgl.nanovg.NVGColor

object Colors {
  val body = createColor(1.0f, 1.0f, 0, 1.0f)
  val orbit = createColor(1.0f, 0.75f, 0, 0.5f)
  val text = createColor(1, 1, 1, 1)

  private def createColor(r: Float, g: Float, b: Float, a: Float): NVGColor = {
    val c = NVGColor.create()
    c.r(r)
    c.g(g)
    c.b(b)
    c.a(a)
  }
}
