package galaxy.widgets

import galaxy.common.{Rect, V2}
import galaxy.rendering.{Colors, RenderContext}

import org.lwjgl.nanovg.NanoVG._

object Label {

  def apply[T](text: String)(implicit rc: RenderContext[T]): Unit = {
    val Rect(V2(x, y), V2(_, h)) = rc.layoutContext.cursor.map(_.toFloat)
    nvgFillColor(rc.nvg, Colors.text)
    val _ = nvgText(rc.nvg, x + 4, y + 0.75f * h, text)
  }
}
