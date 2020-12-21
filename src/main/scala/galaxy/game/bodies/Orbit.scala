package galaxy.game.bodies

import galaxy.common.V2
import galaxy.game.dimensions.Time

final case class Orbit(
  semiMajorAxis: Double,
  eccentricity: Double,
  orbitAngle: Double,
  phaseAtEpoch: Double,
  angularVelocity: Double,
) {
  val semiMinorAxis: Double =
    Some(semiMajorAxis * Math.sqrt(1 - eccentricity * eccentricity))
      .filterNot(_.isNaN)
      .getOrElse(semiMajorAxis)

  val orbitSize: V2[Double] = V2(semiMajorAxis, semiMinorAxis)

  val focusToCenter: V2[Double] =
    (semiMajorAxis * eccentricity) *: V2.unitWithAngle(orbitAngle)

  def orbitalStateRelativeToOriginAt(origin: V2[Double], time: Time): OrbitalState = {
    val phase = (phaseAtEpoch + time.asSeconds.toDouble * angularVelocity) % (2 * Math.PI)
    val center = origin + focusToCenter
    val position = center + V2.rotate(orbitSize * V2.unitWithAngle(phase), orbitAngle)
    OrbitalState(position = position, orbitCenter = center)
  }
}

object Orbit {
  val inert = Orbit(0, 0, 0, 0, 0)
}
