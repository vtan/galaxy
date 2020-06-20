package galaxy.rendering

import galaxy.common.{Rect, V2}

final case class LayoutContext(
  var cursor: Rect[Double]
)

object LayoutContext {

  val initial: LayoutContext = LayoutContext(
    cursor = Rect(V2.zero, V2.zero)
  )
}
