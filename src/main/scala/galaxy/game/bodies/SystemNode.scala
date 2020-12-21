package galaxy.game.bodies

import galaxy.common.{Id, V2}
import galaxy.game.dimensions.Time

final case class SystemNode(
  id: Id[SystemNode],
  orbit: Orbit,
  body: Body,
  children: Vector[SystemNode]
) {
  def orbitalStatesAt(time: Time): Map[Id[SystemNode], OrbitalState] =
    orbitalStateRelativeToOriginAt(origin = V2.zero, time)

  private def orbitalStateRelativeToOriginAt(origin: V2[Double], time: Time): Map[Id[SystemNode], OrbitalState] = {
    val orbitalState = orbit.orbitalStateRelativeToOriginAt(origin, time)
    val childPositions = children.flatMap(
      _.orbitalStateRelativeToOriginAt(origin = orbitalState.position, time)
    ).toMap
    childPositions + (id -> orbitalState)
  }

  def depthFirstSeq: Seq[(SystemNode, Int)] =
    depthFirstSeqFrom(0)

  private def depthFirstSeqFrom(depth: Int): Seq[(SystemNode, Int)] =
    (this, depth) +: children.flatMap(_.depthFirstSeqFrom(depth + 1))
}
