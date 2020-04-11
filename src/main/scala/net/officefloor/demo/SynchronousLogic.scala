package net.officefloor.demo

import java.util.concurrent.Callable
import java.util.function.Supplier

import zio.ZIO
import cats.effect.IO
import reactor.core.publisher.Mono

object SynchronousLogic {

  // START SNIPPET: cats
  def cats(request: ServerRequest)(implicit repository: MessageRepository): IO[ServerResponse] =
    for {
      message <- catsGetMessage(request.getId)
      response = new ServerResponse(s"${message.getContent} via Cats")
    } yield response

  def catsGetMessage(id: Int)(implicit repository: MessageRepository): IO[Message] =
    IO.apply(repository findById id orElseThrow noSuchElement)
  // END SNIPPET: cats


  // START SNIPPET: reactor
  def reactor(request: ServerRequest)(implicit repository: MessageRepository): Mono[ServerResponse] =
    reactorGetMessage(request.getId).map(message => new ServerResponse(s"${message.getContent} via Reactor"))

  def reactorGetMessage(id: Int)(implicit repository: MessageRepository): Mono[Message] =
    Mono.fromCallable(() =>  repository findById id orElseThrow noSuchElement)
  // END SNIPPET: reactor


  // START SNIPPET: zio
  def zio(request: ServerRequest, repository: MessageRepository): ZIO[Any, Throwable, ServerResponse] = {
    // Service logic
    val response = for {
      message <- zioGetMessage(request.getId)
      response = new ServerResponse(s"${message.getContent} via ZIO")
    } yield response

    // Provide dependencies
    response.provide(new InjectMessageRepository {
      override val messageRepository = repository
    })
  }

  def zioGetMessage(id: Int): ZIO[InjectMessageRepository, Throwable, Message] =
    ZIO.accessM(env => ZIO.effect(env.messageRepository.findById(id).orElseThrow(noSuchElement)))

  trait InjectMessageRepository {
    val messageRepository: MessageRepository
  }
  // END SNIPPET: zio


  // START SNIPPET: imperative
  def imperative(request: ServerRequest, repository: MessageRepository): ServerResponse = {
    val message = repository.findById(request.getId).orElseThrow(noSuchElement)
    new ServerResponse(s"${message.getContent} via Imperative")
  }
  // END SNIPPET: imperative

  def noSuchElement(): Supplier[NoSuchElementException] = () => new NoSuchElementException()
}