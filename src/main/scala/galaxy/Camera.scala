package galaxy

final case class Camera(
  worldPosition: V2[Double],
  screenCenter: V2[Double],
  worldToScreenScale: Double
) {
  private val worldtoScreenScaleVector = V2(worldToScreenScale, -worldToScreenScale)

  def scalarToScreen(scalar: Double): Double =
    worldToScreenScale * scalar

  def pointToScreen(point: V2[Double]): V2[Double] =
    (point - worldPosition) * worldtoScreenScaleVector + screenCenter

  def screenToVector(vector: V2[Double]): V2[Double] =
    vector / worldtoScreenScaleVector
}
