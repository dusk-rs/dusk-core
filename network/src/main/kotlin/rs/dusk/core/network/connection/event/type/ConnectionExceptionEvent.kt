package rs.dusk.core.network.connection.event.type

import io.netty.channel.ChannelHandlerContext
import rs.dusk.core.network.connection.event.ConnectionEvent

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 07, 2020
 */
class ConnectionExceptionEvent : ConnectionEvent {
	
	override fun run(ctx : ChannelHandlerContext, cause : Throwable?) {
		cause?.printStackTrace()
	}
	
}