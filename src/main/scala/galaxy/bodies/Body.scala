package galaxy.bodies

import galaxy.Id

final case class Body(
  id: Id[Body],
  name: String
)
