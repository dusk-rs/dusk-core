package rs.dusk.core.network.channel.event.type

import com.github.michaelbull.logging.InlineLogger
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import rs.dusk.core.network.Connectable
import rs.dusk.core.network.channel.event.ChannelEvent

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 07, 2020
 */
class ChannelActiveEvent(
	private val connectable : Connectable,
	private val collection : MutableCollection<Channel>
) : ChannelEvent {
	
	private val logger = InlineLogger()
	
	override fun run(ctx : ChannelHandlerContext, cause : Throwable?) {
		connectable.onConnect(ctx)
		
		logger.debug { "A connection has been registered and added to the set of live connections!" }
		
		collection.add(ctx.channel())
	}
}