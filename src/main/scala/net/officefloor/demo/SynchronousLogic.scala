package net.officefloor.demo

import zio.ZIO
import cats.effect.IO
import reactor.core.publisher.Mono

object SynchronousLogic {

  def cats(request: ServerRequest)(implicit repository: MessageRepository): IO[ServerResponse] =
    for {
      c <- CatsService.getMessage(request.getId)
      response = new ServerResponse(s"${c.getMessage} via Cats")
    } yield response

  def zio(request: ServerRequest, repository: MessageRepository): ZIO[Any, Throwable, ServerResponse] = {
    // Server logic
    val response = for {
      z <- ZioService.getMessage(request.getId)
      response = new ServerResponse(s"${z.getMessage} via ZIO")
    } yield response

    // Provide dependencies
    response.provide(new InjectMessageRepository {
      override val messageRepository = repository
    })
  }

  def reactor(request: ServerRequest)(implicit repository: MessageRepository): Mono[ServerResponse] =
    ReactorService.getMessage(request.getId).map(r => new ServerResponse(s"${r.getMessage} via Reactor"))

  def imperative(request: ServerRequest, repository: MessageRepository): ServerResponse = {
    val message = repository.findById(request.getId).orElseThrow()
    new ServerResponse(s"${message.getMessage} via Imperative")
  }

}