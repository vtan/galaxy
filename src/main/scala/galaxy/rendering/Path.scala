package galaxy.rendering

import galaxy.common.V2

import org.lwjgl.nanovg.NanoVG._
import scala.collection.immutable.ArraySeq

final case class Path(points: ArraySeq[V2[Float]]) extends AnyVal {

  def draw(nvg: Long): Unit =
    points match {
      case head +: tail =>
        nvgMoveTo(nvg, head.x, head.y)
        tail.foreach(p => nvgLineTo(nvg, p.x, p.y))
        nvgLineTo(nvg, head.x, head.y)
      case _ => ()
    }
}

object Path {
  def apply(points: V2[Double]*): Path =
    Path(ArraySeq.from(points.map(_.map(_.toFloat))))
}
