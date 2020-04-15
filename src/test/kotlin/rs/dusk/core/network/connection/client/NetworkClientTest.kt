package rs.dusk.core.network.connection.client

import rs.dusk.core.network.connection.ConnectionPipeline
import rs.dusk.core.network.connection.ConnectionSettings
import rs.dusk.core.network.connection.ReconnectionAdapter

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 25, 2020
 */
fun main() {
    val settings = ConnectionSettings("127.0.0.1", 43593)
    val client = NetworkClient(settings)
    val pipeline = ConnectionPipeline {
        it.addLast("connection.listener", ReconnectionAdapter(client, 10000L, 5))
    }
    client.configure(pipeline)
    client.start()
}