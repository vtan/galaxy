package galaxy.game.bodies

import galaxy.common.Id

import scala.util.Random

object SolarSystem {
  import BodyType._

  val rootSystemNode: SystemNode = SystemNode(
    id = Id(0),
    orbit = Orbit(semiMajorAxis = 0, eccentricity = 0, orbitAngle = 0, phaseAtEpoch = 0, angularVelocity = 0),
    body = Body(name = "Sol", bodyType = Star),
    children = Vector(
      node(name = "Mercury", bodyType = Rocky, semiMajorAxisAu = 0.38, eccentricity = 0.206, angularVelocity = orbitsInYears(0.24)),
      node(name = "Venus", bodyType = Rocky, semiMajorAxisAu = 0.72, eccentricity = 0.007, angularVelocity = orbitsInYears(0.61)),
      node(name = "Earth", bodyType = Rocky, semiMajorAxisAu = 1.00, eccentricity = 0.017, angularVelocity = orbitsInYears(1),
        children = Vector(node(name = "Luna", bodyType = Rocky, semiMajorAxisAu = 0.0026, eccentricity = 0.055, angularVelocity = orbitsInDays(27.32)))
      ),
      node(name = "Mars", bodyType = Rocky, semiMajorAxisAu = 1.52, eccentricity = 0.093, angularVelocity = orbitsInYears(1.88)),
      node(name = "Jupiter", bodyType = GasGiant, semiMajorAxisAu = 5.20, eccentricity = 0.048, angularVelocity = orbitsInYears(11.86),
        children = Vector(
          node(name = "Io", bodyType = Rocky, semiMajorAxisAu = 0.0028, eccentricity = 0.004, angularVelocity = orbitsInDays(1.77)),
          node(name = "Europa", bodyType = Rocky, semiMajorAxisAu = 0.0044, eccentricity = 0.009, angularVelocity = orbitsInDays(3.55)),
          node(name = "Ganymede", bodyType = Rocky, semiMajorAxisAu = 0.0071, eccentricity = 0.001, angularVelocity = orbitsInDays(7.15)),
          node(name = "Callisto", bodyType = Rocky, semiMajorAxisAu = 0.0125, eccentricity = 0.007, angularVelocity = orbitsInDays(16.69))
        )
      ),
      node(name = "Saturn", bodyType = GasGiant, semiMajorAxisAu = 9.53, eccentricity = 0.054, angularVelocity = orbitsInYears(29.44),
        children = Vector(
          node(name = "Mimas", bodyType = Rocky, semiMajorAxisAu = 0.0012, eccentricity = 0.020, angularVelocity = orbitsInDays(0.9)),
          node(name = "Enceladus", bodyType = Rocky, semiMajorAxisAu = 0.0016, eccentricity = 0.004, angularVelocity = orbitsInDays(1.4)),
          node(name = "Tethys", bodyType = Rocky, semiMajorAxisAu = 0.0020, eccentricity = 0.020, angularVelocity = orbitsInDays(1.9)),
          node(name = "Dione", bodyType = Rocky, semiMajorAxisAu = 0.0025, eccentricity = 0.002, angularVelocity = orbitsInDays(2.7)),
          node(name = "Rhea", bodyType = Rocky, semiMajorAxisAu = 0.0040, eccentricity = 0.001, angularVelocity = orbitsInDays(4.5)),
          node(name = "Titan", bodyType = Rocky, semiMajorAxisAu = 0.0081, eccentricity = 0.029, angularVelocity = orbitsInDays(16)),
          node(name = "Iapetus", bodyType = Rocky, semiMajorAxisAu = 0.0238, eccentricity = 0.029, angularVelocity = orbitsInDays(79))
        )
      ),
      node(name = "Uranus", bodyType = IceGiant, semiMajorAxisAu = 19.19, eccentricity = 0.047, angularVelocity = orbitsInYears(84.01)),
      node(name = "Neptune", bodyType = IceGiant, semiMajorAxisAu = 30.06, eccentricity = 0.009, angularVelocity = orbitsInYears(164.79)),
      node(name = "Pluto", bodyType = Rocky, semiMajorAxisAu = 39.48, eccentricity = 0.249, angularVelocity = orbitsInYears(247.94))
    )
  ).assignIds

  private def node(name: String, bodyType: BodyType, semiMajorAxisAu: Double, eccentricity: Double, angularVelocity: Double, children: Vector[SystemNode] = Vector.empty): SystemNode = {
    val random = new Random(name.hashCode)
    val orbitAngle = random.nextDouble()
    val phaseAtEpoch = random.nextDouble()
    val body = Body(name, bodyType)
    val orbit = Orbit(499.05 * semiMajorAxisAu, eccentricity, orbitAngle, phaseAtEpoch, angularVelocity)
    SystemNode(Id(0), orbit, body, children)
  }

  private def orbitsInDays(days: Double): Double =
    2 * Math.PI / (days * 24 * 60 * 60)

  private def orbitsInYears(years: Double): Double =
    orbitsInDays(years * 365)
}
