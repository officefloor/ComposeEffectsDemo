package net.officefloor.demo

import cats.effect.IO
import net.officefloor.frame.api.function.ManagedFunctionContext
import reactor.core.publisher.Mono
import zio.ZIO

object AsynchronousLogic {

  // START SNIPPET: async
  def cats(request: ServerRequest)(implicit repository: AsyncMessageRepository): IO[ServerResponse] =
    for {
      message <- catsGetMessage(request.getId)
      response = new ServerResponse(s"${message.getContent} via Cats")
    } yield response

  def catsGetMessage(id: Int)(implicit repository: AsyncMessageRepository): IO[Message] =
    IO.async(callback => repository.getMessage(id, result => callback.apply(result)))


  def zio(request: ServerRequest, repository: AsyncMessageRepository): ZIO[Any, Throwable, ServerResponse] = {
    // Server logic
    val response = for {
      message <- zioGetMessage(request.getId)
      response = new ServerResponse(s"${message.getContent} via ZIO")
    } yield response

    // Provide dependencies
    response.provide(new InjectAsyncMessageRepository {
      override val asyncMessageRepository = repository
    })
  }

  def zioGetMessage(id: Int): ZIO[InjectAsyncMessageRepository, Throwable, Message] =
    ZIO.accessM(env => ZIO.effectAsync(callback => env.asyncMessageRepository.getMessage(id, result => callback.apply(ZIO.fromEither(result)))))

  trait InjectAsyncMessageRepository {
    val asyncMessageRepository: AsyncMessageRepository
  }


  def reactor(request: ServerRequest)(implicit repository: AsyncMessageRepository): Mono[ServerResponse] =
    reactorGetMessage(request.getId).map(message => new ServerResponse(s"${message.getContent} via Reactor"))

  def reactorGetMessage(id: Int)(implicit repository: AsyncMessageRepository): Mono[Message] =
    Mono.create { callback =>
      repository.getMessage(id, _ match {
        case Left(error) => callback.error(error)
        case Right(message) => callback.success(message)
      })
    }


  def imperative(request: ServerRequest, repository: AsyncMessageRepository, context: ManagedFunctionContext[_, _]): Unit = {
    val async = context.createAsynchronousFlow()
    repository.getMessage(request.getId, _ match {
      case Left(error) => async.complete(() => throw error)
      case Right(message) => async.complete(() => context.setNextFunctionArgument(new ServerResponse(s"${message.getContent} via Imperative")))
    })
  }
  // END SNIPPET: async

}