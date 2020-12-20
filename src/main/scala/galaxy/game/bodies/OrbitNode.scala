package galaxy.game.bodies

import galaxy.common.{Id, V2}

final case class OrbitNode(
  bodyId: Id[Body],
  semiMajorAxis: Double,
  eccentricity: Double,
  orbitAngle: Double,
  phaseAtEpoch: Double,
  angularVelocity: Double,
  children: Vector[OrbitNode]
) {
  val semiMinorAxis: Double =
    Some(semiMajorAxis * Math.sqrt(1 - eccentricity * eccentricity))
      .filterNot(_.isNaN)
      .getOrElse(semiMajorAxis)

  val orbitSize: V2[Double] = V2(semiMajorAxis, semiMinorAxis)

  val focusToCenter: V2[Double] =
    (semiMajorAxis * eccentricity) *: V2.unitWithAngle(orbitAngle)

  def orbitalStatesAt(time: Long): Map[Id[Body], OrbitalState] =
    orbitalStateRelativeToOriginAt(origin = V2.zero, time)

  private def orbitalStateRelativeToOriginAt(origin: V2[Double], time: Long): Map[Id[Body], OrbitalState] = {
    val phase = (phaseAtEpoch + time.toDouble * angularVelocity) % (2 * Math.PI)
    val center = origin + focusToCenter
    val position = center + V2.rotate(orbitSize * V2.unitWithAngle(phase), orbitAngle)
    val childPositions = children.flatMap(_.orbitalStateRelativeToOriginAt(origin = position, time)).toMap
    val orbitalState = OrbitalState(position = position, orbitCenter = center)
    childPositions + (bodyId -> orbitalState)
  }

  def depthFirstSeq: Seq[(OrbitNode, Int)] =
    depthFirstSeqFrom(0)

  private def depthFirstSeqFrom(depth: Int): Seq[(OrbitNode, Int)] =
    (this, depth) +: children.flatMap(_.depthFirstSeqFrom(depth + 1))
}
