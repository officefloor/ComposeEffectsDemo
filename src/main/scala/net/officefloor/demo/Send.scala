package net.officefloor.demo

import net.officefloor.plugin.section.clazz.Parameter
import net.officefloor.web.ObjectResponse

object Send {

  def send(@Parameter serverResponse: ServerResponse, response: ObjectResponse[ServerResponse]): Unit =
    response.send(serverResponse)
}
