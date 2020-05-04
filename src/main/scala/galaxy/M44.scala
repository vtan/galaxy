package galaxy

final case class M44(array: Array[Float]) extends AnyVal {

  def *(rhs: M44): M44 = {
    val result = Array.fill(4 * 4)(0.0f)
    for (i <- 0 until 4) {
      for (j <- 0 until 4) {
        val ix = j + i * 4
        for (k <- 0 until 4) {
          result(ix) = result(ix) + array(k + i * 4) * rhs.array(j + k * 4)
        }
      }
    }
    M44(result)
  }
}

object M44 {

  def ortho(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): M44 =
    M44(Array(
      2 / (right - left), 0, 0, -((right + left) / (right - left)),
      0, 2 / (top - bottom), 0, -((top + bottom) / (top - bottom)),
      0, 0, -2 / (far - near), -((far + near) / far - near),
      0, 0, 0, 1
    ))

  def translation(x: Float, y: Float, z: Float): M44 =
    M44(Array(
      1, 0, 0, x,
      0, 1, 0, y,
      0, 0, 1, z,
      0, 0, 0, 1
    ))

  def scale(x: Float, y: Float, z: Float): M44 =
    M44(Array(
      x, 0, 0, 0,
      0, y, 0, 0,
      0, 0, z, 0,
      0, 0, 0, 1
    ))
}
