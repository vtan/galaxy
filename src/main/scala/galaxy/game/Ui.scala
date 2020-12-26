package galaxy.game

import galaxy.common.{Id, Rect, V2}
import galaxy.game.bodies.{GalaxyGenerator, SystemGenerator, SystemList}
import galaxy.game.dimensions.TimeDiff
import galaxy.rendering.RenderContext
import galaxy.widgets.{Button, Label}

import scala.util.Random

object Ui {

  private val timeOptions: Seq[(String, Option[TimeDiff])] = Seq(
    "stop" -> None,
    "1min" -> Some(TimeDiff(1)),
    "1h" -> Some(TimeDiff(60)),
    "12h" -> Some(TimeDiff(12 * 60)),
    "5d" -> Some(TimeDiff(5 * 24 * 60)),
    "30d" -> Some(TimeDiff(30 * 24 * 60))
  )

  def render()(implicit rc: RenderContext[AppState]): Unit = {
    if (rc.appState.uiState.systemMapOpen) {
      rc.layoutContext.cursor = Rect(V2(8, 8), V2(120, 24))
      Button[AppState]("View galaxy", _.mapUiState(_.copy(systemMapOpen = false)))

      rc.layoutContext.cursor = Rect(V2(128, 8), V2(120, 24))
      Button[AppState]("Generate", _.mapGameState { gs =>
        val starSystem = gs.starSystems(rc.appState.uiState.selectedStarSystem)
        val newRoot = SystemGenerator.generate(starSystem.name)(new Random())
        gs.copy(starSystems = gs.starSystems + (starSystem.id -> starSystem.copy(rootNode = newRoot)))
      })

      rc.layoutContext.cursor = Rect(V2(8, 8 + 24 + 8), V2(120, 24))
      val selectedSystemNode = {
        val globalNode = rc.appState.uiState.selectedSystemNode
        val starSystem = rc.appState.gameState.starSystems(globalNode.starSystemId)
        starSystem.nodesById(globalNode.systemNodeId)
      }
      Label("Selected: " + selectedSystemNode.body.name)

      rc.layoutContext.cursor = Rect(V2(8, 8 + 24 + 8 + 32), V2(120, 24))
      SystemList.render()
    } else {
      rc.layoutContext.cursor = Rect(V2(8, 8), V2(120, 24))
      Button[AppState]("View system", _.mapUiState(_.copy(systemMapOpen = true)))

      rc.layoutContext.cursor = Rect(V2(8, 8 + 24 + 8), V2(120, 24))
      Label("Selected: " + rc.appState.gameState.starSystems(rc.appState.uiState.selectedStarSystem).name)

      rc.layoutContext.cursor = Rect(V2(128, 8), V2(120, 24))
      Button[AppState]("Generate", _.mapGameState { gs =>
        val starSystems = GalaxyGenerator.generate(new Random())
        gs.copy(starSystems = starSystems)
      }.mapUiState { ui =>
        ui.copy(selectedStarSystem = Id(0))
      })
    }

    timeOptions.zipWithIndex.foreach {
      case ((label, speed), index) =>
        rc.layoutContext.cursor = Rect(V2(rc.screenSize.x - 8 - 6 * 60 + index.toDouble * 60, 8), V2(60, 24))
        Button[AppState](label, _.mapUiState(_.copy(updateSpeed = speed)))
    }
    rc.layoutContext.cursor = Rect(V2(1540, 8 + 24 + 8), V2(120, 24))
    Label(rc.appState.gameState.time.toString)
  }
}
