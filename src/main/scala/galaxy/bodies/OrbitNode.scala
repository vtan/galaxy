package galaxy.bodies

import galaxy.{Id, V2}

final case class OrbitNode(
  bodyId: Id[Body],
  orbitRadius: Double,
  phaseAtEpoch: Double,
  angularVelocity: Double,
  children: Vector[OrbitNode]
) {

  def orbitalStatesAt(time: Double): Map[Id[Body], OrbitalState] =
    orbitalStateRelativeToOriginAt(origin = V2.zero, time)

  def orbitalStateRelativeToOriginAt(origin: V2[Double], time: Double): Map[Id[Body], OrbitalState] = {
    val phase = (phaseAtEpoch + time * angularVelocity) % (2 * Math.PI)
    val position = origin + orbitRadius *: V2.unitWithAngle(phase)
    val childPositions = children.flatMap(_.orbitalStateRelativeToOriginAt(origin = position, time)).toMap
    val orbitalState = OrbitalState(position = position, orbitCenter = origin)
    childPositions + (bodyId -> orbitalState)
  }

  def toDepthFirstSeq: Seq[OrbitNode] =
    this +: children.flatMap(_.toDepthFirstSeq)
}
