package rs.dusk.core.network.connection.event

import com.github.michaelbull.logging.InlineLogger
import io.netty.channel.ChannelHandlerContext

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 02, 2020
 */
open class ConnectionEventChain {
	
	private val logger = InlineLogger()
	
	/**
	 * A map of each [Event Type][ConnectionEventType], which holds a list of the possible [connection events][ConnectionEvent] that it may invoke
	 */
	private val events = hashMapOf<ConnectionEventType, MutableList<ConnectionEvent>>()
	
	/**
	 * The addition of an event to the [event type][ConnectionEventType] list of events
	 */
	fun append(type : ConnectionEventType, event : ConnectionEvent) {
		val list = events[type] ?: mutableListOf()
		list.add(event)
		events[type] = list
	}
	
	/**
	 * This method invokes all [events][ConnectionEvent] for the specified type
	 */
	fun handle(type : ConnectionEventType, ctx : ChannelHandlerContext, error : Throwable? = null) {
		val connectionEvents = events[type]
		if (connectionEvents == null) {
			logger.warn { "No connection events of type [$type] were registered!" }
			return
		}
		connectionEvents.forEach { event ->
			event.run(ctx, error)
		}
	}
	
}