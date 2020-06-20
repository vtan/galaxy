package galaxy.common

final case class Rect[T](
  position: V2[T],
  size: V2[T]
) {

  def contains(point: V2[T])(implicit ord: Ordering[T], num: Numeric[T]): Boolean =
    ord.gteq(point.x, position.x) && ord.gteq(point.y, position.y) &&
    ord.lt(point.x, num.plus(position.x, size.x)) && ord.lt(point.y, num.plus(position.y, size.y))

  def map[U](f: T => U): Rect[U] =
    Rect(position.map(f), size.map(f))
}
