package net.officefloor.demo

import reactor.core.publisher.Mono

object ReactorService {

  def getMessage(id: Int)(implicit repository: MessageRepository): Mono[Message] =
    Mono.fromCallable(() => repository.findById(id).orElseThrow())

}