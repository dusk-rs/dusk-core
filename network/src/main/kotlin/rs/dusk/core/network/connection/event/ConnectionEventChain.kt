package rs.dusk.core.network.connection.event

import io.netty.channel.ChannelHandlerContext

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since May 02, 2020
 */
open class ConnectionEventChain {

    /**
     * A map of each [Event Type][ConnectionEventType], which holds a list of the possible [connection events][ConnectionEvent] that it may invoke
     */
    private val events = hashMapOf<ConnectionEventType, MutableList<ConnectionEvent>>()

    /**
     * The addition of an event to the [event type][ConnectionEventType] list of events
     */
    fun append(type: ConnectionEventType, event: ConnectionEvent) {
        if (events.containsKey(type)) {
            throw IllegalStateException("Duplicate event type registration attempt [type=$type]")
        }
        val list = events[type] ?: mutableListOf()
        list.add(event)
        events[type] = list
    }

    /**
     * This method invokes all [events][ConnectionEvent] for the specified type
     */
    fun handle(type: ConnectionEventType, ctx: ChannelHandlerContext, error: Throwable? = null) {
        events[type]?.forEach { event ->
            event.run(ctx, error)
        }
    }

}