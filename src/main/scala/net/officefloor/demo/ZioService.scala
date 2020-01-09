package net.officefloor.demo

import zio.ZIO

object ZioService {

  def getMessage(id: Int): ZIO[InjectMessageRepository, Throwable, Message] =
    ZIO.accessM(env => ZIO.effect(env.messageRepository.findById(id).orElseThrow()))
}
