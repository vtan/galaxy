package galaxy

sealed trait GlfwEvent

final case class CursorPositionEvent(
  x: Double,
  y: Double,
  xDiff: Double,
  yDiff: Double
) extends GlfwEvent

final case class MouseButtonEvent(
  button: Int,
  action: Int,
  modifiers: Int
) extends GlfwEvent

final case class ScrollEvent(
  x: Double,
  y: Double
) extends GlfwEvent
