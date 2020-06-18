package galaxy

import org.lwjgl.nanovg.NanoVG._
import org.lwjgl.opengl.GL11C._

object Renderer {

  def render()(implicit rc: RenderContext): Unit = {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

    nvgBeginFrame(rc.nvg, rc.screenSize.x.toFloat, rc.screenSize.y.toFloat, 1)
    nvgFontSize(rc.nvg, 20)

    SystemMap.render()

    nvgEndFrame(rc.nvg)
  }
}
