package galaxy.game.bodies

import galaxy.common.Id

import scala.util.Random

object SolarSystem {

  val bodies: Map[Id[Body], Body] = {
    import BodyType._
    Seq(
      Body(id = Id(8), name = "Sol", bodyType = Star),
      Body(id = Id(0), name = "Mercury", bodyType = Rocky),
      Body(id = Id(1), name = "Venus", bodyType = Rocky),
      Body(id = Id(2), name = "Earth", bodyType = Rocky),
      Body(id = Id(9), name = "Luna", bodyType = Rocky),
      Body(id = Id(3), name = "Mars", bodyType = Rocky),
      Body(id = Id(10), name = "Phobos", bodyType = Rocky),
      Body(id = Id(11), name = "Deimos", bodyType = Rocky),
      Body(id = Id(4), name = "Jupiter", bodyType = GasGiant),
      Body(id = Id(12), name = "Io", bodyType = Rocky),
      Body(id = Id(13), name = "Europa", bodyType = Rocky),
      Body(id = Id(14), name = "Ganymede", bodyType = Rocky),
      Body(id = Id(15), name = "Callisto", bodyType = Rocky),
      Body(id = Id(5), name = "Saturn", bodyType = GasGiant),
      Body(id = Id(16), name = "Mimas", bodyType = Rocky),
      Body(id = Id(17), name = "Enceladus", bodyType = Rocky),
      Body(id = Id(18), name = "Tethys", bodyType = Rocky),
      Body(id = Id(19), name = "Dione", bodyType = Rocky),
      Body(id = Id(20), name = "Rhea", bodyType = Rocky),
      Body(id = Id(21), name = "Titan", bodyType = Rocky),
      Body(id = Id(22), name = "Iapetus", bodyType = Rocky),
      Body(id = Id(6), name = "Uranus", bodyType = IceGiant),
      Body(id = Id(7), name = "Neptune", bodyType = IceGiant),
      Body(id = Id(23), name = "Pluto", bodyType = Rocky)
    ).map(b => b.id -> b).toMap
  }

  val rootOrbitNode: OrbitNode = OrbitNode(
    bodyId = Id(8),
    semiMajorAxis = 0,
    eccentricity = 0,
    orbitAngle = 0,
    phaseAtEpoch = 0,
    angularVelocity = 0,
    children = Vector(
      node(id = 0, semiMajorAxis = 0.38, eccentricity = 0.206, angularVelocity = orbitsInYears(0.24)),
      node(id = 1, semiMajorAxis = 0.72, eccentricity = 0.007, angularVelocity = orbitsInYears(0.61)),
      node(id = 2, semiMajorAxis = 1.00, eccentricity = 0.017, angularVelocity = orbitsInYears(1),
        children = Vector(node(id = 9, semiMajorAxis = 0.0026, eccentricity = 0.055, angularVelocity = orbitsInDays(27.32)))
      ),
      node(id = 3, semiMajorAxis = 1.52, eccentricity = 0.093, angularVelocity = orbitsInYears(1.88),
        children = Vector(
          node(id = 10, semiMajorAxis = 6.2e-5, eccentricity = 0.015, angularVelocity = orbitsInDays(0.318)),
          node(id = 11, semiMajorAxis = 0.00015, eccentricity = 0, angularVelocity = orbitsInDays(1.263))
        )
      ),
      node(id = 4, semiMajorAxis = 5.20, eccentricity = 0.048, angularVelocity = orbitsInYears(11.86),
        children = Vector(
          node(id = 12, semiMajorAxis = 0.0028, eccentricity = 0.004, angularVelocity = orbitsInDays(1.77)),
          node(id = 13, semiMajorAxis = 0.0044, eccentricity = 0.009, angularVelocity = orbitsInDays(3.55)),
          node(id = 14, semiMajorAxis = 0.0071, eccentricity = 0.001, angularVelocity = orbitsInDays(7.15)),
          node(id = 15, semiMajorAxis = 0.0125, eccentricity = 0.007, angularVelocity = orbitsInDays(16.69))
        )
      ),
      node(id = 5, semiMajorAxis = 9.53, eccentricity = 0.054, angularVelocity = orbitsInYears(29.44),
        children = Vector(
          node(id = 16, semiMajorAxis = 0.0012, eccentricity = 0.020, angularVelocity = orbitsInDays(0.9)),
          node(id = 17, semiMajorAxis = 0.0016, eccentricity = 0.004, angularVelocity = orbitsInDays(1.4)),
          node(id = 18, semiMajorAxis = 0.0020, eccentricity = 0.020, angularVelocity = orbitsInDays(1.9)),
          node(id = 19, semiMajorAxis = 0.0025, eccentricity = 0.002, angularVelocity = orbitsInDays(2.7)),
          node(id = 20, semiMajorAxis = 0.0040, eccentricity = 0.001, angularVelocity = orbitsInDays(4.5)),
          node(id = 21, semiMajorAxis = 0.0081, eccentricity = 0.029, angularVelocity = orbitsInDays(16)),
          node(id = 22, semiMajorAxis = 0.0238, eccentricity = 0.029, angularVelocity = orbitsInDays(79))
        )
      ),
      node(id = 6, semiMajorAxis = 19.19, eccentricity = 0.047, angularVelocity = orbitsInYears(84.01)),
      node(id = 7, semiMajorAxis = 30.06, eccentricity = 0.009, angularVelocity = orbitsInYears(164.79)),
      node(id = 23, semiMajorAxis = 39.48, eccentricity = 0.249, angularVelocity = orbitsInYears(247.94))
    )
  )

  private def node(id: Long, semiMajorAxis: Double, eccentricity: Double, angularVelocity: Double, children: Vector[OrbitNode] = Vector.empty): OrbitNode = {
    val random = new Random(id)
    val orbitAngle = random.nextDouble()
    val phaseAtEpoch = random.nextDouble()
    OrbitNode(Id[Body](id), semiMajorAxis, eccentricity, orbitAngle, phaseAtEpoch, angularVelocity, children)
  }

  private def orbitsInDays(days: Double): Double =
    2 * Math.PI / (days * 24 * 60 * 60)

  private def orbitsInYears(years: Double): Double =
    orbitsInDays(years * 365)
}
