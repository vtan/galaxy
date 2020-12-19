package galaxy.game

import galaxy.common.{Rect, V2}
import galaxy.game.bodies.SystemList
import galaxy.rendering.RenderContext
import galaxy.widgets.Button

import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoVG._
import org.lwjgl.nanovg.NanoVGGL3._
import org.lwjgl.opengl.GL11C._

object Renderer {
  private val framebufferPaint: NVGPaint = NVGPaint.create()

  def render()(implicit rc: RenderContext[AppState]): Unit = {
    rc.layoutContext.cursor = Rect(V2(8, 8), V2(120, 24))

    nvgluBindFramebuffer(rc.nvg, rc.uiFramebuffer)
    frame {
      Button("Hello button", identity[AppState])
      rc.layoutContext.cursor = rc.layoutContext.cursor.copy(position = rc.layoutContext.cursor.position + V2(0, 32))
      SystemList.render()
    }
    nvgluBindFramebuffer(rc.nvg, null)

    frame {
      SystemMap.render()
      nvgImagePattern(rc.nvg, 0, 0, rc.screenSize.x, rc.screenSize.y, 0, rc.uiFramebuffer.image, 1, framebufferPaint)
      nvgBeginPath(rc.nvg)
      nvgRect(rc.nvg, 0, 0, rc.screenSize.x, rc.screenSize.y)
      nvgFillPaint(rc.nvg, framebufferPaint)
      nvgFill(rc.nvg)
    }
  }

  private def frame(f: => Unit)(implicit rc: RenderContext[_]): Unit = {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT)
    nvgBeginFrame(rc.nvg, rc.screenSize.x, rc.screenSize.y, rc.pixelRatio)
    nvgFontSize(rc.nvg, 20)
    f
    nvgEndFrame(rc.nvg)
  }
}
