package net.officefloor.demo

import cats.effect.IO
import net.officefloor.plugin.section.clazz.Parameter
import reactor.core.publisher.Mono
import zio.ZIO

object ComposeLogic {

  // START SNIPPET: compose
  def seed: String = "Hi"

  def cats(@Parameter param: String): IO[String] = IO.pure(s"$param, via Cats")

  def reactor(@Parameter param: String): Mono[String] = Mono.just(s"$param, via Reactor")

  def zio(@Parameter param: String): ZIO[Any, Nothing, String] = ZIO.succeed(s"$param, via ZIO")

  def imperative(@Parameter param: String): String = s"$param, via Imperative"

  def response(@Parameter message: String): ServerResponse = new ServerResponse(message)
  // END SNIPPET: compose

}
