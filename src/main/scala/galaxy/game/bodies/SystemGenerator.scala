package galaxy.game.bodies

import galaxy.common.Id

import scala.util.Random

object SystemGenerator {

  def generate(implicit random: Random): SystemNode = {
    val systemName = generateSystemName
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
      body = Body("", BodyType.Star),
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

  private def generateSystemName(implicit random: Random): String =
    generateSystemNames.head

  private def generateSystemNames(implicit random: Random): Seq[String] = {
    random.shuffle(
      constellations.flatMap { constellation =>
        val chosenLetters: Seq[String] = random.shuffle(letters).take(10)
        val chosenNumbers: Seq[String] = random.shuffle(1 to 99).take(5).map(_.toString)
        (chosenLetters ++ chosenNumbers).map(_ + " " + constellation)
      }
    )
  }

  private def constellations = Seq(
    "Andromedae", "Antliae", "Apodis", "Aquarii", "Aquilae", "Arae", "Arietis", "Aurigae", "Boötis", "Caeli",
    "Camelopardalis", "Cancri", "Canum Venaticorum", "Canis Majoris", "Canis Minoris", "Capricorni", "Carinae",
    "Cassiopeiae", "Centauri", "Cephei", "Ceti", "Chamaeleontis", "Circini", "Columbae", "Comae Berenices",
    "Coronae Australis", "Coronae Borealis", "Corvi", "Crateris", "Crucis", "Cygni", "Delphini", "Doradus",
    "Draconis", "Equulei", "Eridani", "Fornacis", "Geminorum", "Gruis", "Herculis", "Horologii", "Hydrae", "Hydri",
    "Indi", "Lacertae", "Leonis", "Leonis Minoris", "Leporis", "Librae", "Lupi", "Lyncis", "Lyrae", "Mensae",
    "Microscopii", "Monocerotis", "Muscae", "Normae", "Octantis", "Ophiuchi", "Orionis", "Pavonis", "Pegasi",
    "Persei", "Phoenicis", "Pictoris", "Piscium", "Piscis Austrini", "Puppis", "Pyxidis", "Reticuli", "Sagittae",
    "Sagittarii", "Scorpii", "Sculptoris", "Scuti", "Serpentis", "Sextantis", "Tauri", "Telescopii", "Trianguli",
    "Trianguli Australis", "Tucanae", "Ursae Majoris", "Ursae Minoris", "Velorum", "Virginis", "Volantis", "Vulpeculae"
  )

  private def letters = Seq(
    "Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Kappa", "Lambda", "Mu", "Nu",
    "Xi", "Omicron", "Pi", "Rho", "Sigma", "Tau", "Upsilon", "Phi", "Chi", "Psi", "Omega"
  )
}
