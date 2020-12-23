package galaxy.game.bodies

import galaxy.common.{Id, V2}

import scala.collection.immutable.LazyList
import scala.util.Random

object GalaxyGenerator {

  private val radius: Int = 10

  def generate(implicit random: Random): Map[Id[StarSystem], StarSystem] = {
    val radiusSq = radius * radius
    val coords = for {
      x <- -radius to radius
      y <- -radius to radius
      if x * x + y * y <= radiusSq
      if !(x == 0 && y == 0)
      dx = random.nextDouble() - 0.5
      dy = random.nextDouble() - 0.5
    } yield 6 *: V2(x.toDouble + dx, y.toDouble + dy)
    val nameStream = LazyList.from(generateSystemNames) ++ LazyList.continually("???")

    val starSystems = (coords zip nameStream).zipWithIndex.map {
      case ((position, name), index) =>
        val rootNode = SystemGenerator.generate(name)
        StarSystem(
          id = Id(index.toLong + 1),
          name = name,
          position = position,
          rootNode = rootNode,
          jumpPoints = Vector.empty
        )
    }

    val sol = StarSystem(
      id = Id(0),
      name = "Sol",
      position = V2.zero,
      rootNode = SolarSystem.rootSystemNode,
      jumpPoints = Vector.empty
    )

    addJumpPoints(sol +: starSystems)
      .map(s => s.id -> s).toMap
  }

  private def addJumpPoints(starSystems: Seq[StarSystem]): Seq[StarSystem] = {
    val thresholdSq = 8.5 * 8.5
    starSystems.map { starSystem =>
      val neighbors = starSystems
        .filter(_.id != starSystem.id)
        .filter(other => (other.position - starSystem.position).lengthSq <= thresholdSq)
      val jumpPoints = neighbors.map { neighbor =>
        JumpPoint(
          destination = neighbor.id,
          position = 2000 *: (neighbor.position - starSystem.position)
        )
      }.toVector
      starSystem.copy(jumpPoints = jumpPoints)
    }
  }

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
