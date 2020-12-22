package galaxy.game.bodies

import galaxy.common.Id

import scala.util.Random

object SystemGenerator {

  def generate(systemName: String)(implicit random: Random): SystemNode = {
    val planetCount = random.nextInt(13)
    val (planets, _) = (1 to planetCount).foldLeft((Vector.empty[SystemNode], 100.0)) {
      case ((planets, distanceFromStar), index) =>
        val bodyType = random.nextInt(5) match {
          case 0 => BodyType.GasGiant
          case 1 => BodyType.IceGiant
          case _ => BodyType.Rocky
        }
        val minMargin = bodyType match {
          case BodyType.Rocky => 50.0
          case _ => 1000.0
        }
        val marginBefore = generateMargin(minMargin)
        val marginAfter = generateMargin(minMargin)
        val planet = generatePlanet(systemName, bodyType, distanceFromStar + marginBefore, index)
        (planets :+ planet, distanceFromStar + marginBefore + marginAfter)
    }
    SystemNode(
      id = Id(0),
      orbit = Orbit.inert,
      body = Body(systemName, BodyType.Star),
      children = planets
    ).assignIds
  }

  private def generatePlanet(systemName: String, bodyType: BodyType, semiMajorAxis: Double, index: Int)(implicit random: Random): SystemNode = {
    val orbit = Orbit(
      semiMajorAxis = semiMajorAxis,
      eccentricity = Math.abs(random.nextGaussian()) / 30,
      orbitAngle = random.nextDouble() * Math.PI * 2,
      phaseAtEpoch = random.nextDouble() * Math.PI * 2,
      angularVelocity = orbitsInDays(Math.sqrt(133_333 * Math.pow(semiMajorAxis / 500, 3)))
    )
    SystemNode(
      id = Id(0),
      orbit = orbit,
      body = Body(s"$systemName $index", bodyType = bodyType),
      children = Vector.empty
    )
  }

  private def generateMargin(min: Double)(implicit random: Random): Double =
    min * (1 + 9 * random.nextDouble())

  private def orbitsInDays(days: Double): Double =
    2 * Math.PI / (days * 24 * 60 * 60)
}
