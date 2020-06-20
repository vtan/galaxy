package galaxy.widgets

import galaxy.common.{Rect, V2}
import galaxy.rendering.{Colors, MouseButtonEvent, RenderContext}

import org.lwjgl.glfw.GLFW._
import org.lwjgl.nanovg.NanoVG._

object Button {

  def apply[T](text: String, onClick: T => T)(implicit rc: RenderContext[T]): Unit = {
    val clicked = rc.consumeEvents {
      case MouseButtonEvent(GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS, _, x, y)
        if rc.layoutContext.cursor.contains(V2(x, y)) =>
          rc.dispatch(onClick)
    }.nonEmpty

    val Rect(V2(x, y), V2(w, h)) = rc.layoutContext.cursor.map(_.toFloat)
    nvgBeginPath(rc.nvg)
    nvgRect(rc.nvg, x, y, w, h)
    nvgFillColor(rc.nvg, if (clicked) Colors.buttonActive else Colors.button)
    nvgFill(rc.nvg)
    nvgStrokeColor(rc.nvg, Colors.buttonBorder)
    nvgStroke(rc.nvg)

    nvgFillColor(rc.nvg, Colors.text)
    val _ = nvgText(rc.nvg, x + 4, y + 0.75f * h, text)
  }
}
