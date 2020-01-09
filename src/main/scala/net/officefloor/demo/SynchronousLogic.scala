package net.officefloor.demo

import cats.effect.IO
import reactor.core.publisher.Mono
import zio.ZIO

object SynchronousLogic {

  def cats(request: ServerRequest)(implicit repository: MessageRepository): IO[ServerResponse] =
    for {
      c <- CatsService.getMessage(request.id)
      response = ServerResponse(c.getMessage + " via Cats")
    } yield response

  def zio(request: ServerRequest, repository: MessageRepository): ZIO[Any, Throwable, ServerResponse] = {
    // Server logic
    val response = for {
      z <- ZioService.getMessage(request.id)
      response = ServerResponse(z.getMessage + "via ZIO")
    } yield response

    // Provide dependencies
    response.provide(new InjectMessageRepository {
      override val messageRepository = repository
    })
  }

  def reactor(request: ServerRequest)(implicit repository: MessageRepository): Mono[ServerResponse] =
    ReactorService.getMessage(request.id).map(r => ServerResponse(r.getMessage + " via Reactor"))

  def imperative(request: ServerRequest, repository: MessageRepository): ServerResponse = {
    val message = repository.findById(request.id).orElseThrow()
    ServerResponse(message.getMessage + " via Imperative")
  }

}