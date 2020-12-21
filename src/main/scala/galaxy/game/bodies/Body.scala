package galaxy.game.bodies

final case class Body(
  name: String,
  bodyType: BodyType
)

sealed trait BodyType

object BodyType {
  case object Star extends BodyType
  case object Rocky extends BodyType
  case object GasGiant extends BodyType
  case object IceGiant extends BodyType
}
