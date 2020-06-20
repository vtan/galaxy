package galaxy.game

import galaxy.common.{Rect, V2}
import galaxy.rendering.RenderContext
import galaxy.widgets.Button

import org.lwjgl.nanovg.NanoVG._
import org.lwjgl.opengl.GL11C._

object Renderer {

  def render()(implicit rc: RenderContext[AppState]): Unit = {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

    nvgBeginFrame(rc.nvg, rc.screenSize.x.toFloat, rc.screenSize.y.toFloat, 1)
    nvgFontSize(rc.nvg, 20)

    rc.layoutContext.cursor = Rect(V2(8, 8), V2(120, 24))
    Button("Hello button", identity[AppState])
    SystemMap.render()

    nvgEndFrame(rc.nvg)
  }
}
