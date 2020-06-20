package galaxy.rendering

import galaxy.common.V2

import org.lwjgl.nanovg.NVGLUFramebuffer

final case class RenderContext[T](
  nvg: Long,
  pixelRatio: Float,
  screenSize: V2[Float],

  appState: T,
  var dispatchedUpdates: List[T => T],
  var events: List[GlfwEvent],

  layoutContext: LayoutContext,
  uiFramebuffer: NVGLUFramebuffer
) {

  def dispatch(update: T => T): Unit =
    dispatchedUpdates = update :: dispatchedUpdates

  def beginFrame(events: List[GlfwEvent]): RenderContext[T] =
    copy(
      appState = Function.chain(dispatchedUpdates.reverse)(appState),
      dispatchedUpdates = List.empty,
      events = events,
      layoutContext = LayoutContext.initial
    )

  def consumeEvents[A](matcher: PartialFunction[GlfwEvent, A]): Seq[A] = {
    val (remaining, results) = events.partitionMap(event => matcher.unapply(event).toRight(event))
    events = remaining
    results
  }
}
