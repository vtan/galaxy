package galaxy.common

final case class Camera(
  worldPosition: V2[Double],
  screenCenter: V2[Double],
  worldToScreenScale: Double
) {
  private val worldToScreenScaleVector = V2(worldToScreenScale, -worldToScreenScale)

  def scalarToScreen(scalar: Double): Double =
    worldToScreenScale * scalar

  def screenToScalar(scalar: Double): Double =
    scalar / worldToScreenScale

  def pointToScreen(point: V2[Double]): V2[Double] =
    (point - worldPosition) * worldToScreenScaleVector + screenCenter

  def screenToVector(vector: V2[Double]): V2[Double] =
    vector / worldToScreenScaleVector

  def screenToPoint(point: V2[Double]): V2[Double] =
    worldPosition + (point - screenCenter) / worldToScreenScaleVector
}
