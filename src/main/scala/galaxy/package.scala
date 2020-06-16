package object galaxy {

  def clamp(min: Double, x: Double, max: Double): Double =
    if (x < min) {
      min
    } else if (x > max) {
      max
    } else {
      x
    }
}
