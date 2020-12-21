package galaxy.game

import galaxy.common.{Rect, V2}
import galaxy.game.bodies.SystemList
import galaxy.game.dimensions.TimeDiff
import galaxy.rendering.RenderContext
import galaxy.widgets.{Button, Label}

import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoVG._
import org.lwjgl.nanovg.NanoVGGL3._
import org.lwjgl.opengl.GL11C._

object Renderer {
  private val framebufferPaint: NVGPaint = NVGPaint.create()

  private val timeOptions: Seq[(String, Option[TimeDiff])] = Seq(
    "stop" -> None,
    "1min" -> Some(TimeDiff(1)),
    "1h" -> Some(TimeDiff(60)),
    "12h" -> Some(TimeDiff(12 * 60)),
    "5d" -> Some(TimeDiff(5 * 24 * 60)),
    "30d" -> Some(TimeDiff(30 * 24 * 60))
  )

  def render()(implicit rc: RenderContext[AppState]): Unit = {
    rc.layoutContext.cursor = Rect(V2(8, 8), V2(120, 24))

    nvgluBindFramebuffer(rc.nvg, rc.uiFramebuffer)
    frame {
      Button("Hello button", identity[AppState])
      rc.layoutContext.cursor = rc.layoutContext.cursor.copy(position = rc.layoutContext.cursor.position + V2(0, 32))
      SystemList.render()

      timeOptions.zipWithIndex.foreach {
        case ((label, speed), index) =>
          rc.layoutContext.cursor = Rect(V2(rc.screenSize.x - 8 - 6 * 60 + index.toDouble * 60, 8), V2(60, 24))
          Button[AppState](label, _.mapGameState(_.copy(updateSpeed = speed)))
      }
      rc.layoutContext.cursor = Rect(V2(1540, 8 + 24 + 8), V2(120, 24))
      Label(rc.appState.gameState.time.toString)
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

    rc.dispatch(_.mapGameState(StepLogic.stepTime))
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
