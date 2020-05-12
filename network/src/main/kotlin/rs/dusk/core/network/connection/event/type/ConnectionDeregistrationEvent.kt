package rs.dusk.core.network.connection.event.type

import com.github.michaelbull.logging.InlineLogger
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import rs.dusk.core.network.NetworkClient
import rs.dusk.core.network.connection.event.ConnectionEvent

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 07, 2020
 */
class ConnectionDeregistrationEvent(
	private val client : NetworkClient,
	private val collection : MutableCollection<Channel>
) : ConnectionEvent {
	
	private val logger = InlineLogger()
	
	override fun run(ctx : ChannelHandlerContext, cause : Throwable?) {
		client.connected = false
		
		logger.info { "A connection has been de-registered and removed from total list!" }
		
		collection.remove(ctx.channel())
	}
}