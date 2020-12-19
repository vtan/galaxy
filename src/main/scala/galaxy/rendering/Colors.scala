package galaxy.rendering

import galaxy.game.bodies.BodyType

import org.lwjgl.nanovg.NVGColor

object Colors {
  val orbit = createColor(0.8, 0.75, 0.6, 0.5)
  val text = createColor(1, 1, 1)

  val bodyColors: Function[BodyType, NVGColor] = {
    case BodyType.Star => createColor(1, 1, 0)
    case BodyType.Rocky => createColor(0.8, 0.8, 0.7)
    case BodyType.GasGiant => createColor(1, 0.8, 0.2)
    case BodyType.IceGiant => createColor(0.2, 0.8, 1)
  }

  val button = createColor(0.4, 0.4, 0.4)
  val buttonActive = createColor(0.8, 0.8, 0.4)
  val buttonBorder = createColor(0.6, 0.6, 0.6)

  private def createColor(r: Double, g: Double, b: Double, a: Double = 1): NVGColor = {
    val c = NVGColor.create()
    c.r(r.toFloat)
    c.g(g.toFloat)
    c.b(b.toFloat)
    c.a(a.toFloat)
  }
}
