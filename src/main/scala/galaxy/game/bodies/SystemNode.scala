package galaxy.game.bodies

import galaxy.common.{Id, V2}
import galaxy.game.dimensions.Time

import scala.collection.mutable

final case class SystemNode(
  id: Id[SystemNode],
  orbit: Orbit,
  body: Body,
  children: Vector[SystemNode]
) {
  def orbitalStatesAt(time: Time): Map[Id[SystemNode], OrbitalState] =
    orbitalStateRelativeToOriginAt(origin = V2.zero, time)

  private def orbitalStateRelativeToOriginAt(origin: V2[Double], time: Time): Map[Id[SystemNode], OrbitalState] = {
    val orbitalState = orbit.orbitalStateRelativeToOriginAt(origin, time)
    val childPositions = children.flatMap(
      _.orbitalStateRelativeToOriginAt(origin = orbitalState.position, time)
    ).toMap
    childPositions + (id -> orbitalState)
  }

  def assignIds: SystemNode = assignIdsFrom(0)._1

  private def assignIdsFrom(from: Long): (SystemNode, Long) = {
    val (updated, idAfter) = children.foldLeft((Vector.empty[SystemNode], from)) {
      case ((acc, nextId), node) =>
        val (updated, idAfter) = node.assignIdsFrom(nextId)
        (acc :+ updated, idAfter)
    }
    (copy(id = Id(idAfter), children = updated), idAfter + 1)
  }

  def treeIterator: Iterator[SystemNode] =
    Iterator(this) ++ children.iterator.flatMap(_.treeIterator)

  def depthFirstSeq: Seq[(SystemNode, Int)] =
    depthFirstSeqFrom(0)

  private def depthFirstSeqFrom(depth: Int): Seq[(SystemNode, Int)] =
    (this, depth) +: children.flatMap(_.depthFirstSeqFrom(depth + 1))

  def breadthFirstSeq: Seq[SystemNode] = {
    val queue = mutable.Queue(this)
    val builder = Seq.newBuilder[SystemNode]
    while (queue.nonEmpty) {
      val head = queue.dequeue()
      builder += head
      queue.enqueueAll(head.children)
    }
    builder.result()
  }
}
