package org.redrune.core.network.connection.client

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import org.redrune.core.network.connection.ConnectionPipeline
import org.redrune.core.network.connection.ConnectionSettings
import org.redrune.core.network.connection.ReconnectionAdapter

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 25, 2020
 */
fun main() {
    val settings = ConnectionSettings("127.0.0.1", 43593)
    val client = NetworkClient(settings)
    val pipeline = ConnectionPipeline {
        it.addLast("connection.listener", ReconnectionAdapter(client, 10_000L, 5))
    }
    client.configure(pipeline)
    client.start()

}