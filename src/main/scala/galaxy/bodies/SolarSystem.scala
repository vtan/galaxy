package galaxy.bodies

import galaxy.Id

object SolarSystem {

  private val pi: Double = Math.PI

  val rootOrbitNode: OrbitNode = OrbitNode(
    bodyId = Id(8),
    orbitRadius = 0,
    phaseAtEpoch = 0,
    angularVelocity = 0,
    children = Vector(
      OrbitNode(bodyId = Id(0), orbitRadius = 0.38, angularVelocity = 2 * pi / (0.24 * 365 * 24 * 60 * 60), phaseAtEpoch = 5.183077334009456, children = Vector.empty),
      OrbitNode(bodyId = Id(1), orbitRadius = 0.72, angularVelocity = 2 * pi / (0.61 * 365 * 24 * 60 * 60), phaseAtEpoch = 1.2630917966904205, children = Vector.empty),
      OrbitNode(bodyId = Id(2), orbitRadius = 1.00, angularVelocity = 2 * pi / (365 * 24 * 60 * 60), phaseAtEpoch = 3.150585230297741,
        children = Vector(OrbitNode(bodyId = Id(9), orbitRadius = 0.0026, angularVelocity = 2 * pi / (27.32 * 24 * 60 * 60), phaseAtEpoch = 1.1052205264497115, children = Vector.empty))
      ),
      OrbitNode(bodyId = Id(3), orbitRadius = 1.52, angularVelocity = 2 * pi / (1.88 * 365 * 24 * 60 * 60), phaseAtEpoch = 4.992778300672891,
        children = Vector(OrbitNode(bodyId = Id(10), orbitRadius = 6.2e-5, angularVelocity = 2 * pi / (0.318 * 24 * 60 * 60), phaseAtEpoch = 4.951196230681527, children = Vector.empty),
          OrbitNode(bodyId = Id(11), orbitRadius = 0.00015, angularVelocity = 2 * pi / (1.263 * 24 * 60 * 60), phaseAtEpoch = 3.1702750214086004, children = Vector.empty)
        )
      ),
      OrbitNode(bodyId = Id(4), orbitRadius = 5.20, angularVelocity = 2 * pi / (11.86 * 365 * 24 * 60 * 60), phaseAtEpoch = 4.389498795147114,
        children = Vector(
          OrbitNode(bodyId = Id(12), orbitRadius = 0.0028, angularVelocity = 2 * pi / (1.77 * 24 * 60 * 60), phaseAtEpoch = 0.9847674707864948, children = Vector.empty),
          OrbitNode(bodyId = Id(13), orbitRadius = 0.0044, angularVelocity = 2 * pi / (3.55 * 24 * 60 * 60), phaseAtEpoch = 4.192675332309923, children = Vector.empty),
          OrbitNode(bodyId = Id(14), orbitRadius = 0.0071, angularVelocity = 2 * pi / (7.15 * 24 * 60 * 60), phaseAtEpoch = 2.7159649277748605, children = Vector.empty),
          OrbitNode(bodyId = Id(15), orbitRadius = 0.0125, angularVelocity = 2 * pi / (16.69 * 24 * 60 * 60), phaseAtEpoch = 4.856683528845399, children = Vector.empty)
        )
      ),
      OrbitNode(bodyId = Id(5), orbitRadius = 9.53, angularVelocity = 2 * pi / (29.44 * 365 * 24 * 60 * 60), phaseAtEpoch = 1.3941826461020987,
        children = Vector(
          OrbitNode(bodyId = Id(16), orbitRadius = 0.0012, angularVelocity = 2 * pi / (0.9 * 24 * 60 * 60), phaseAtEpoch = 5.5490471982273215, children = Vector.empty),
          OrbitNode(bodyId = Id(17), orbitRadius = 0.0016, angularVelocity = 2 * pi / (1.4 * 24 * 60 * 60), phaseAtEpoch = 1.9301490174419995, children = Vector.empty),
          OrbitNode(bodyId = Id(18), orbitRadius = 0.0020, angularVelocity = 2 * pi / (1.9 * 24 * 60 * 60), phaseAtEpoch = 5.270329258859241, children = Vector.empty),
          OrbitNode(bodyId = Id(19), orbitRadius = 0.0025, angularVelocity = 2 * pi / (2.7 * 24 * 60 * 60), phaseAtEpoch = 4.654234941931287, children = Vector.empty),
          OrbitNode(bodyId = Id(20), orbitRadius = 0.0040, angularVelocity = 2 * pi / (4.5 * 24 * 60 * 60), phaseAtEpoch = 4.903102665762759, children = Vector.empty),
          OrbitNode(bodyId = Id(21), orbitRadius = 0.0081, angularVelocity = 2 * pi / (16 * 24 * 60 * 60), phaseAtEpoch = 1.403922883894238, children = Vector.empty),
          OrbitNode(bodyId = Id(22), orbitRadius = 0.0238, angularVelocity = 2 * pi / (79 * 24 * 60 * 60), phaseAtEpoch = 5.90320859341022, children = Vector.empty)
        )
      ),
      OrbitNode(bodyId = Id(6), orbitRadius = 19.19, angularVelocity = 2 * pi / (84.01 * 365 * 24 * 60 * 60), phaseAtEpoch = 5.034906979201371, children = Vector.empty),
      OrbitNode(bodyId = Id(7), orbitRadius = 30.06, angularVelocity = 2 * pi / (164.79 * 365 * 24 * 60 * 60), phaseAtEpoch = 3.7836608026774474, children = Vector.empty)
    )
  )
}
