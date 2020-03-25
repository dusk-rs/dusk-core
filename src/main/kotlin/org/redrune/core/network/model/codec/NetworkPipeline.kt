package org.redrune.core.network.model.codec

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import org.redrune.core.network.connection.ReconnectionAdapter
import org.redrune.core.network.connection.client.NetworkClient

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 25, 2020
 */
class NetworkPipeline : ChannelInitializer<SocketChannel>() {

    private val handlers = hashMapOf<String, ChannelHandler>()

    fun addConnectionListener(client: NetworkClient, retryDelay: Long, retryCapacity: Int) {
        addHandler("connection.listener", ReconnectionAdapter(client, retryDelay, retryCapacity))
    }

    /**
     * Stores a handler in the [handlers] map if possible
     */
    fun addHandler(name: String, handler: ChannelHandler) {
        if (handlers.containsKey(name)) {
            throw IllegalStateException("Unable to register handler $name - predefined entry existed [${handlers[name]}")
        }
        handlers[name] = handler
    }

    override fun initChannel(ch: SocketChannel) {
        handlers.forEach { ch.pipeline().addLast(it.key, it.value) }
    }

}