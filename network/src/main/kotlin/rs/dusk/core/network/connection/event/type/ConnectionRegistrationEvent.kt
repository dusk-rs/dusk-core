package rs.dusk.core.network.connection.event.type

import com.github.michaelbull.logging.InlineLogger
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import rs.dusk.core.network.connection.event.ConnectionEvent

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 07, 2020
 */
class ConnectionRegistrationEvent(private val collection : MutableCollection<Channel>) : ConnectionEvent {
	
	private val logger = InlineLogger()
	
	override fun run(ctx : ChannelHandlerContext, cause : Throwable?) {
		logger.debug { "A connection has been registered and added to the set of live connections!" }
		collection.remove(ctx.channel())
	}
}