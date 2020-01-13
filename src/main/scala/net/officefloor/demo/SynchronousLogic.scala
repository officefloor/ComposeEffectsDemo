package net.officefloor.demo

import zio.ZIO
import cats.effect.IO
import reactor.core.publisher.Mono

object SynchronousLogic {

  def cats(request: ServerRequest)(implicit repository: MessageRepository): IO[ServerResponse] =
    for {
      c <- catsGetMessage(request.getId)
      response = new ServerResponse(s"${c.getMessage} via Cats")
    } yield response

  def catsGetMessage(id: Int)(implicit repository: MessageRepository): IO[Message] =
    IO.apply(repository findById id orElseThrow)


  def zio(request: ServerRequest, repository: MessageRepository): ZIO[Any, Throwable, ServerResponse] = {
    // Server logic
    val response = for {
      z <- zioGetMessage(request.getId)
      response = new ServerResponse(s"${z.getMessage} via ZIO")
    } yield response

    // Provide dependencies
    response.provide(new InjectMessageRepository {
      override val messageRepository = repository
    })
  }

  def zioGetMessage(id: Int): ZIO[InjectMessageRepository, Throwable, Message] =
    ZIO.accessM(env => ZIO.effect(env.messageRepository.findById(id).orElseThrow()))

  trait InjectMessageRepository {
    val messageRepository: MessageRepository
  }


  def reactor(request: ServerRequest)(implicit repository: MessageRepository): Mono[ServerResponse] =
    reactorGetMessage(request.getId).map(r => new ServerResponse(s"${r.getMessage} via Reactor"))

  def reactorGetMessage(id: Int)(implicit repository: MessageRepository): Mono[Message] =
    Mono.fromCallable(() => repository.findById(id).orElseThrow())


  def imperative(request: ServerRequest, repository: MessageRepository): ServerResponse = {
    val message = repository.findById(request.getId).orElseThrow()
    new ServerResponse(s"${message.getMessage} via Imperative")
  }

}