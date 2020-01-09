package net.officefloor.demo

import net.officefloor.web.HttpObject

/**
 * Request on HTTP server.
 *
 * @param id Identifier of message.
 */
@HttpObject
case class ServerRequest(id: Int)
