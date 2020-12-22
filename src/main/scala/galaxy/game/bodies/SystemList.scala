package galaxy.game.bodies

import galaxy.common.{Rect, V2}
import galaxy.game.AppState
import galaxy.rendering.{Colors, RenderContext}

import org.lwjgl.nanovg.NanoVG._

object SystemList {

  def render()(implicit rc: RenderContext[AppState]): Unit = {
    val gs = rc.appState.gameState
    val selectedStarSystem = gs.starSystems(rc.appState.uiState.selectedStarSystem)
    val lines = selectedStarSystem.rootNode.depthFirstSeq.map {
      case (node, depth) =>
        (1 to depth).map(_ => "  |   ").mkString ++ node.body.name
    }

    val Rect(V2(x, y), _) = rc.layoutContext.cursor.map(_.toFloat)
    nvgFillColor(rc.nvg, Colors.text)
    lines.zipWithIndex.foreach {
      case (line, index) =>
        val yLine = y + (index.toFloat + 0.75f) * 16
        val _ = nvgText(rc.nvg, x, yLine, line)
    }
  }
}
