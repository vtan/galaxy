package galaxy

final case class V2[T](x: T, y: T) {

  def +(rhs: V2[T])(implicit num: Numeric[T]): V2[T] =
    V2(num.plus(x, rhs.x), num.plus(y, rhs.y))

  def -(rhs: V2[T])(implicit num: Numeric[T]): V2[T] =
    V2(num.minus(x, rhs.x), num.minus(y, rhs.y))

  def *(rhs: V2[T])(implicit num: Numeric[T]): V2[T] =
    V2(num.times(x, rhs.x), num.times(y, rhs.y))

  def /(rhs: V2[T])(implicit frac: Fractional[T]): V2[T] =
    V2(frac.div(x, rhs.x), frac.div(y, rhs.y))

  def *:(scalar: T)(implicit num: Numeric[T]): V2[T] =
    V2(num.times(scalar, x), num.times(scalar, y))

  def map[U](f: T => U): V2[U] =
    V2(f(x), f(y))
}

object V2 {

  def zero[T](implicit num: Numeric[T]): V2[T] =
    V2(num.zero, num.zero)

  def unitWithAngle(angle: Double):  V2[Double] =
    V2(Math.cos(angle), Math.sin(angle))
}
