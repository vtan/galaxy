package galaxy.game.bodies

import galaxy.common.{Id, V2}

final case class OrbitNode(
  bodyId: Id[Body],
  orbitRadius: Double,
  phaseAtEpoch: Double,
  angularVelocity: Double,
  children: Vector[OrbitNode]
) {

  def orbitalStatesAt(time: Long): Map[Id[Body], OrbitalState] =
    orbitalStateRelativeToOriginAt(origin = V2.zero, time)

  private def orbitalStateRelativeToOriginAt(origin: V2[Double], time: Long): Map[Id[Body], OrbitalState] = {
    val phase = (phaseAtEpoch + time.toDouble * angularVelocity) % (2 * Math.PI)
    val position = origin + orbitRadius *: V2.unitWithAngle(phase)
    val childPositions = children.flatMap(_.orbitalStateRelativeToOriginAt(origin = position, time)).toMap
    val orbitalState = OrbitalState(position = position, orbitCenter = origin)
    childPositions + (bodyId -> orbitalState)
  }

  def depthFirstSeq: Seq[(OrbitNode, Int)] =
    depthFirstSeqFrom(0)

  private def depthFirstSeqFrom(depth: Int): Seq[(OrbitNode, Int)] =
    (this, depth) +: children.flatMap(_.depthFirstSeqFrom(depth + 1))
}
