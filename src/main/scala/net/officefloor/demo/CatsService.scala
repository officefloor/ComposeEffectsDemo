package net.officefloor.demo

import cats.effect.IO

object CatsService {

  def getMessage(id: Int)(implicit repository: MessageRepository): IO[Message] =
    IO.apply(repository findById id orElseThrow)

}