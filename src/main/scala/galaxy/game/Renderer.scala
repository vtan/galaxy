package galaxy.game

import galaxy.rendering.RenderContext

import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoVG._
import org.lwjgl.nanovg.NanoVGGL3._
import org.lwjgl.opengl.GL11C._

object Renderer {
  private val framebufferPaint: NVGPaint = NVGPaint.create()

  def render()(implicit rc: RenderContext[AppState]): Unit = {
    nvgluBindFramebuffer(rc.nvg, rc.uiFramebuffer)
    frame {
      Ui.render()
    }
    nvgluBindFramebuffer(rc.nvg, null)

    frame {
      if (rc.appState.uiState.systemMapOpen) {
        SystemMap.render()
      } else {
        GalaxyMap.render()
      }
      nvgImagePattern(rc.nvg, 0, 0, rc.screenSize.x, rc.screenSize.y, 0, rc.uiFramebuffer.image, 1, framebufferPaint)
      nvgBeginPath(rc.nvg)
      nvgRect(rc.nvg, 0, 0, rc.screenSize.x, rc.screenSize.y)
      nvgFillPaint(rc.nvg, framebufferPaint)
      nvgFill(rc.nvg)
    }

    rc.appState.uiState.updateSpeed match {
      case Some(timeStep) =>
        rc.dispatch(_.mapGameState(StepLogic.stepTime(timeStep)))
      case None =>
        ()
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
