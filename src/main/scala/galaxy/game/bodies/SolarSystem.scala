package galaxy.game.bodies

import galaxy.common.Id

object SolarSystem {

  private val pi: Double = Math.PI

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
      Body(id = Id(7), name = "Neptune", bodyType = IceGiant)
    ).map(b => b.id -> b).toMap
  }

  val rootOrbitNode: OrbitNode = OrbitNode(
    bodyId = Id(8),
    orbitRadius = 0,
    phaseAtEpoch = 0,
    angularVelocity = 0,
    children = Vector(
      OrbitNode(bodyId = Id(0), orbitRadius = 0.38, angularVelocity = orbitsInYears(0.24), phaseAtEpoch = 5.183077334009456, children = Vector.empty),
      OrbitNode(bodyId = Id(1), orbitRadius = 0.72, angularVelocity = orbitsInYears(0.61), phaseAtEpoch = 1.2630917966904205, children = Vector.empty),
      OrbitNode(bodyId = Id(2), orbitRadius = 1.00, angularVelocity = orbitsInYears(1), phaseAtEpoch = 3.150585230297741,
        children = Vector(OrbitNode(bodyId = Id(9), orbitRadius = 0.0026, angularVelocity = orbitsInDays(27.32), phaseAtEpoch = 1.1052205264497115, children = Vector.empty))
      ),
      OrbitNode(bodyId = Id(3), orbitRadius = 1.52, angularVelocity = orbitsInYears(1.88), phaseAtEpoch = 4.992778300672891,
        children = Vector(
          OrbitNode(bodyId = Id(10), orbitRadius = 6.2e-5, angularVelocity = orbitsInDays(0.318), phaseAtEpoch = 4.951196230681527, children = Vector.empty),
          OrbitNode(bodyId = Id(11), orbitRadius = 0.00015, angularVelocity = orbitsInDays(1.263), phaseAtEpoch = 3.1702750214086004, children = Vector.empty)
        )
      ),
      OrbitNode(bodyId = Id(4), orbitRadius = 5.20, angularVelocity = orbitsInYears(11.86), phaseAtEpoch = 4.389498795147114,
        children = Vector(
          OrbitNode(bodyId = Id(12), orbitRadius = 0.0028, angularVelocity = orbitsInDays(1.77), phaseAtEpoch = 0.9847674707864948, children = Vector.empty),
          OrbitNode(bodyId = Id(13), orbitRadius = 0.0044, angularVelocity = orbitsInDays(3.55), phaseAtEpoch = 4.192675332309923, children = Vector.empty),
          OrbitNode(bodyId = Id(14), orbitRadius = 0.0071, angularVelocity = orbitsInDays(7.15), phaseAtEpoch = 2.7159649277748605, children = Vector.empty),
          OrbitNode(bodyId = Id(15), orbitRadius = 0.0125, angularVelocity = orbitsInDays(16.69), phaseAtEpoch = 4.856683528845399, children = Vector.empty)
        )
      ),
      OrbitNode(bodyId = Id(5), orbitRadius = 9.53, angularVelocity = orbitsInYears(29.44), phaseAtEpoch = 1.3941826461020987,
        children = Vector(
          OrbitNode(bodyId = Id(16), orbitRadius = 0.0012, angularVelocity = orbitsInDays(0.9), phaseAtEpoch = 5.5490471982273215, children = Vector.empty),
          OrbitNode(bodyId = Id(17), orbitRadius = 0.0016, angularVelocity = orbitsInDays(1.4), phaseAtEpoch = 1.9301490174419995, children = Vector.empty),
          OrbitNode(bodyId = Id(18), orbitRadius = 0.0020, angularVelocity = orbitsInDays(1.9), phaseAtEpoch = 5.270329258859241, children = Vector.empty),
          OrbitNode(bodyId = Id(19), orbitRadius = 0.0025, angularVelocity = orbitsInDays(2.7), phaseAtEpoch = 4.654234941931287, children = Vector.empty),
          OrbitNode(bodyId = Id(20), orbitRadius = 0.0040, angularVelocity = orbitsInDays(4.5), phaseAtEpoch = 4.903102665762759, children = Vector.empty),
          OrbitNode(bodyId = Id(21), orbitRadius = 0.0081, angularVelocity = orbitsInDays(16), phaseAtEpoch = 1.403922883894238, children = Vector.empty),
          OrbitNode(bodyId = Id(22), orbitRadius = 0.0238, angularVelocity = orbitsInDays(79), phaseAtEpoch = 5.90320859341022, children = Vector.empty)
        )
      ),
      OrbitNode(bodyId = Id(6), orbitRadius = 19.19, angularVelocity = orbitsInYears(84.01), phaseAtEpoch = 5.034906979201371, children = Vector.empty),
      OrbitNode(bodyId = Id(7), orbitRadius = 30.06, angularVelocity = orbitsInYears(164.79), phaseAtEpoch = 3.7836608026774474, children = Vector.empty)
    )
  )

  private def orbitsInDays(days: Double): Double =
    2 * pi / (days * 24 * 60 * 60)

  private def orbitsInYears(years: Double): Double =
    orbitsInDays(years * 365)
}
