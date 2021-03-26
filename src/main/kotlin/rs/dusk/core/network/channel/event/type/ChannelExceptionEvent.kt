package rs.dusk.core.network.channel.event.type

import io.netty.channel.ChannelHandlerContext
import rs.dusk.core.network.channel.event.ChannelEvent

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 07, 2020
 */
class ChannelExceptionEvent : ChannelEvent {
	
	override fun run(ctx : ChannelHandlerContext, cause : Throwable?) {
		cause?.printStackTrace()
	}
	
}